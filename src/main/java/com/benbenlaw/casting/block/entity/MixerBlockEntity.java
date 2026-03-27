package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.MixerBlock;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.custom.MixingRecipe;
import com.benbenlaw.casting.screen.MixerMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.FilterFluidHandler;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import com.benbenlaw.core.block.entity.handler.fluid.OutputFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class MixerBlockEntity extends SyncableBlockEntity implements MenuProvider, FluidSending, FluidAccepting {

    private final ContainerData data;
    private int maxProgress = 200;
    private int progress = 0;

    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 4, 8000, (i, stack) -> i <= 3);
    private final OutputFluidHandler outputFluidHandler = new OutputFluidHandler(this, 1, 16000, i -> i == 0);
    private FilterFluidHandler filterFluidHandler = new FilterFluidHandler(this, 4);

    private RecipeHolder<MixingRecipe> cachedRecipes;

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.MIXER_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MixerBlockEntity.this.progress;
                    case 1 -> MixerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MixerBlockEntity.this.progress = value;
                    case 1 -> MixerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        boolean isRunning = level.getBlockState(worldPosition).getValue(MixerBlock.RUNNING);

        if (!isRunning) {
            updateWorkingState(false);
            if (this.progress > 0) {
                this.progress = 0;
                setChanged();
                sync();
            }
            return;
        }

        RecipeHolder<MixingRecipe> recipeHolder = getRecipe();
        boolean changed = false;
        boolean isCurrentlyWorking = false;

        if (recipeHolder != null) {
            MixingRecipe recipe = recipeHolder.value();

            if (canFormOutput(recipe)) {
                isCurrentlyWorking = true;
                this.progress++;
                changed = true;

                if (this.progress >= this.maxProgress) {
                    executeMixing(recipe);
                    this.progress = 0;
                }
            } else {
                if (this.progress > 0) {
                    this.progress = 0;
                    changed = true;
                }
            }
        } else {
            if (this.progress > 0) {
                this.progress = 0;
                changed = true;
            }
        }

        updateWorkingState(isCurrentlyWorking);
        this.tickResourceSending(level, worldPosition);

        if (changed) {
            setChanged();
            sync();
        }
    }

    private void updateWorkingState(boolean working) {
        BlockState currentState = level.getBlockState(worldPosition);
        if (currentState.getValue(CastingBlock.WORKING) != working) {
            level.setBlock(worldPosition, currentState.setValue(CastingBlock.WORKING, working), 3);
        }
    }

    private boolean canFormOutput(MixingRecipe recipe) {
        FluidStack output = recipe.outputFluid().create();
        try (Transaction tx = Transaction.open(null)) {
            long inserted = outputFluidHandler.insertInternal(0, FluidResource.of(output), output.getAmount(), tx);
            return inserted == output.getAmount();
        }
    }

    private void executeMixing(MixingRecipe recipe) {
        try (Transaction tx = Transaction.open(null)) {
            for (SizedFluidIngredient required : recipe.fluids()) {
                int remainingToDrain = required.amount();
                for (int i = 0; i < 4 && remainingToDrain > 0; i++) {
                    FluidStack inTank = FluidUtil.getStack(inputFluidHandler, i);

                    if (required.ingredient().test(inTank)) {
                        int drained = inputFluidHandler.extractInternal(i, FluidResource.of(inTank), remainingToDrain, tx);
                        remainingToDrain -= drained;
                    }
                }

                if (remainingToDrain > 0) return;
            }

            FluidStack outputStack = recipe.outputFluid().create();
            outputFluidHandler.insertInternal(
                    0,
                    FluidResource.of(outputStack),
                    outputStack.getAmount(),
                    tx
            );

            tx.commit();
        }
    }

    private RecipeHolder<MixingRecipe> getRecipe() {
        if (level == null || level.getServer() == null) return null;

        return level.getServer().getRecipeManager()
                .recipeMap()
                .values()
                .stream()
                .filter(holder -> holder.value().getType() == MixingRecipe.TYPE)
                .map(holder -> (RecipeHolder<MixingRecipe>) holder)
                .filter(holder -> {
                    MixingRecipe recipe = holder.value();
                    for (SizedFluidIngredient required : recipe.fluids()) {
                        if (!hasFluidSatisfyingIngredient(required)) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst()
                .orElse(null);
    }

    private boolean hasFluidSatisfyingIngredient(SizedFluidIngredient required) {
        int totalFound = 0;

        for (int i = 0; i < 4; i++) {
            FluidStack inTank = FluidUtil.getStack(inputFluidHandler, i);

            if (required.ingredient().test(inTank)) {
                totalFound += inTank.getAmount();
            }
        }

        return totalFound >= required.amount();
    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, getFluidCapability());
    }

    public InputFluidHandler getInputFluidHandler() {
        return inputFluidHandler;
    }

    public OutputFluidHandler getOutputFluidHandler() {
        return outputFluidHandler;
    }

    public ResourceHandler<FluidResource> getFluidCapability() {
        return new CombinedResourceHandler<>(inputFluidHandler, outputFluidHandler);
    }

    public FilterFluidHandler getFilterFluidHandler() {
        return filterFluidHandler;
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, Inventory inventory, Player player) {
        return new MixerMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.casting.mixer");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {

        inputFluidHandler.serialize(output.child("inputFluids"));
        outputFluidHandler.serialize(output.child("outputFluid"));
        filterFluidHandler.serialize(output.child("filterFluids"));
        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);

        super.saveAdditional(output);
    }


    @Override
    protected void loadAdditional(ValueInput input) {

        inputFluidHandler.deserialize(input.childOrEmpty("inputFluids"));
        outputFluidHandler.deserialize(input.childOrEmpty("outputFluid"));
        filterFluidHandler.deserialize(input.childOrEmpty("filterFluids"));
        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 200);

        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(inputFluidHandler, outputFluidHandler));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            System.out.println(component.fluids());
            component.applyToHandlers(inputFluidHandler, outputFluidHandler);
        }
    }

    @Override
    public InputFluidHandler receivingHandler() {
        return inputFluidHandler;
    }

    @Override
    public @Nullable FilterFluidHandler getFilter() {
        return filterFluidHandler;
    }

    @Override
    public OutputFluidHandler sendingHandler() {
        return outputFluidHandler;
    }
}