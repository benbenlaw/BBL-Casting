package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.SolidifierBlock;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.MixingRecipe;
import com.benbenlaw.casting.recipe.SolidifierRecipe;
import com.benbenlaw.casting.screen.SolidifierMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.CombinedItemHandler;
import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import com.benbenlaw.core.block.entity.handler.item.OutputItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class SolidifierBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final ContainerData data;
    private int maxProgress = 200;
    private int progress = 0;

    private final InputItemHandler inputHandler = new InputItemHandler(this, 1, (i, stack) -> i == 0);
    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 1, 16000, (i, stack) -> i == 0);
    private final OutputItemHandler outputHandler = new OutputItemHandler(this, 1, i -> i == 0);

    private RecipeHolder<MixingRecipe> cachedRecipes;

    public SolidifierBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.SOLIDIFIER_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SolidifierBlockEntity.this.progress;
                    case 1 -> SolidifierBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SolidifierBlockEntity.this.progress = value;
                    case 1 -> SolidifierBlockEntity.this.maxProgress = value;
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

        boolean isRunning = level.getBlockState(worldPosition).getValue(SolidifierBlock.RUNNING);

        if (!isRunning) {
            updateWorkingState(false);
            if (this.progress > 0) {
                this.progress = 0;
                setChanged();
                sync();
            }
            return;
        }

        boolean changed = false;
        boolean isCurrentlyWorking = false;

        RecipeHolder<SolidifierRecipe> recipeHolder = getRecipe();

        if (recipeHolder != null) {
            SolidifierRecipe recipe = recipeHolder.value();

            if (canFormOutput(recipe) && hasEnoughFluid(recipe)) {
                isCurrentlyWorking = true;
                this.progress++;
                changed = true;

                if (this.progress >= this.maxProgress) {
                    executeSolidifying(recipe);
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

    private boolean hasEnoughFluid(SolidifierRecipe recipe) {
        FluidStack inTank = FluidUtil.getStack(inputFluidHandler, 0);
        return !inTank.isEmpty() &&
                FluidStack.isSameFluidSameComponents(inTank, recipe.fluid()) &&
                inTank.getAmount() >= recipe.fluid().amount();
    }

    private boolean canFormOutput(SolidifierRecipe recipe) {
        ItemStack recipeOutput = getStackFromSized(recipe.output());
        if (recipeOutput.isEmpty()) return false;
        try (Transaction tx = Transaction.open(null)) {
            long inserted = outputHandler.insertInternalReturn(
                    0,
                    ItemResource.of(recipeOutput),
                    recipeOutput.getCount(),
                    tx
            );

            return inserted == recipeOutput.getCount();
        }
    }

    private void executeSolidifying(SolidifierRecipe recipe) {
        try (Transaction tx = Transaction.open(null)) {
            inputFluidHandler.extractInternal(0, FluidResource.of(recipe.fluid()), recipe.fluid().amount(), tx);
            ItemStack result = getStackFromSized(recipe.output());

            if (!result.isEmpty()) {
                outputHandler.insertInternal(0, ItemResource.of(result), result.getCount(), tx);
            }

            tx.commit();
        }
    }

    private ItemStack getStackFromSized(SizedIngredient sizedIngredient) {
        return sizedIngredient.ingredient().items()
                .findFirst()
                .map(holder -> new ItemStack(holder.value(), sizedIngredient.count()))
                .orElse(ItemStack.EMPTY);
    }

    private RecipeHolder<SolidifierRecipe> getRecipe() {
        if (level == null || level.getServer() == null) return null;

        ItemStack mold = ItemUtil.getStack(inputHandler, 0);
        FluidStack fluid = FluidUtil.getStack(inputFluidHandler, 0);

        if (mold.isEmpty() || fluid.isEmpty()) return null;

        return level.getServer().getRecipeManager()
                .recipeMap()
                .values()
                .stream()
                .filter(holder -> holder.value().getType() == SolidifierRecipe.TYPE)
                .map(holder -> (RecipeHolder<SolidifierRecipe>) holder)
                .filter(holder -> holder.value().mold().test(mold) &&
                        FluidStack.isSameFluidSameComponents(holder.value().fluid().create(), fluid))
                .findFirst()
                .orElse(null);
    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, inputFluidHandler);
    }

    public InputItemHandler getInputHandler() {
        return inputHandler;
    }

    public InputFluidHandler getInputFluidHandler() {
        return inputFluidHandler;
    }

    public OutputItemHandler getOutputHandler() {
        return outputHandler;
    }

    public ResourceHandler<ItemResource> getItemCapability() {
        return new CombinedItemHandler(inputHandler, outputHandler);
    }

    public ResourceHandler<FluidResource> getFluidCapability() {
        return inputFluidHandler;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, Inventory inventory, Player player) {
        return new SolidifierMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.casting.solidifier");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {

        inputHandler.serialize(output.child("input"));
        inputFluidHandler.serialize(output.child("inputFluid"));
        outputHandler.serialize(output.child("output"));
        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);

        super.saveAdditional(output);
    }


    @Override
    protected void loadAdditional(ValueInput input) {

        inputHandler.deserialize(input.childOrEmpty("input"));
        inputFluidHandler.deserialize(input.childOrEmpty("inputFluid"));
        outputHandler.deserialize(input.childOrEmpty("output"));
        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 200);

        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        dropInventoryContents(inputHandler);
        dropInventoryContents(outputHandler);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(inputFluidHandler));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            System.out.println(component.fluids());
            component.applyToHandlers(inputFluidHandler);
        }
    }

}