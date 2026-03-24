package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.ControllerBlock;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.CastingRecipes;
import com.benbenlaw.casting.recipe.MeltingRecipe;
import com.benbenlaw.casting.screen.ControllerMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.OutputFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ControllerBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final ContainerData data;
    private int[] progress = new int[15];
    private int[] maxProgress = new int[15];

    private final InputItemHandler inputHandler = new InputItemHandler(this, 15, (i, stack) -> i >= 0 && i <= 14);
    private final OutputFluidHandler outputFluidHandler = new OutputFluidHandler(this, 4, 16000, i -> i >= 0 && i <= 3);

    private RecipeHolder<MeltingRecipe>[] cachedRecipes;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.CONTROLLER_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            public int get(int index) {
                if (index < 15) {
                    return progress[index];
                } else if (index < 30) {
                    return maxProgress[index - 15];
                }
                return 0;
            }

            public void set(int index, int value) {
                if (index < 15) {
                    progress[index] = value;
                } else if (index < 30) {
                    maxProgress[index - 15] = value;
                }
            }

            public int getCount() {
                return 30;
            }
        };
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        boolean isRunning = level.getBlockState(worldPosition).getValue(ControllerBlock.RUNNING);

        if (!isRunning) {
            updateWorkingState(false);
            return;
        }

        boolean changed = false;
        boolean isWorking = false;

        for (int i = 0; i < 15; i++) {
            ItemStack stack = inputHandler.getResource(i).toStack();

            if (stack.isEmpty()) {
                if (progress[i] > 0) {
                    progress[i] = 0;
                    changed = true;
                }
                continue;
            }

            RecipeHolder<MeltingRecipe> recipeHolder = getRecipeForSlot(stack);

            if (recipeHolder != null) {
                MeltingRecipe recipe = recipeHolder.value();
                this.maxProgress[i] = recipe.meltingTemp();

                if (canFitFluids(recipe.output())) {
                    isWorking = true;
                    progress[i]++;
                    changed = true;

                    if (progress[i] >= maxProgress[i]) {
                        executeMelting(i, recipe);
                        progress[i] = 0;
                    }
                }
            } else {
                if (progress[i] > 0) {
                    progress[i] = 0;
                    changed = true;
                }
            }
        }

        updateWorkingState(isWorking);

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

    private boolean canFitFluids(List<FluidStackTemplate> outputs) {
        for (FluidStackTemplate fluidToInsert : outputs) {
            int needed = fluidToInsert.amount();
            int tankToUse = -1;

            for (int i = 0; i < 4; i++) {
                FluidStack existing = FluidUtil.getStack(outputFluidHandler, i);
                if (!existing.isEmpty() && FluidStack.isSameFluidSameComponents(existing, fluidToInsert)) {
                    tankToUse = i;
                    break;
                }
            }

            if (tankToUse == -1) {
                for (int i = 0; i < 4; i++) {
                    if (FluidUtil.getStack(outputFluidHandler, i).isEmpty()) {
                        tankToUse = i;
                        break;
                    }
                }
            }

            if (tankToUse != -1) {
                FluidStack currentInTank = FluidUtil.getStack(outputFluidHandler, tankToUse);
                int capacity = outputFluidHandler.getCapacityAsInt(tankToUse, FluidResource.of(FluidUtil.getStack(outputFluidHandler, tankToUse)));
                int currentAmount = currentInTank.getAmount();

                if (currentAmount + needed > capacity) {
                    return false; // Not enough room
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private void executeMelting(int slot, MeltingRecipe recipe) {
        try (Transaction tx = Transaction.open(null)) {
            inputHandler.extractInternal(slot, ItemResource.of(inputHandler.getResource(slot).toStack()), recipe.input().count(), tx);

            for (FluidStackTemplate fluid : recipe.output()) {
                int remaining = fluid.amount();
                for (int tank = 0; tank < 4 && remaining > 0; tank++) {
                    remaining -= outputFluidHandler.insertInternal(tank, FluidResource.of(fluid), remaining, tx);
                }
            }

            tx.commit();
        }
    }

    private RecipeHolder<MeltingRecipe> getRecipeForSlot(ItemStack stack) {
        if (level == null || level.getServer() == null || stack.isEmpty()) return null;

        return level.getServer().getRecipeManager()
                .recipeMap()
                .values()
                .stream()
                .filter(holder -> holder.value().getType() == MeltingRecipe.TYPE)
                .map(holder -> (RecipeHolder<MeltingRecipe>) holder)
                .filter(holder -> holder.value().input().test(stack))
                .findFirst()
                .orElse(null);
    }

    public InputItemHandler getInputHandler() {
        return inputHandler;
    }

    public ResourceHandler<ItemResource> getItemCapability() {
        return inputHandler;
    }

    public OutputFluidHandler getOutputFluidHandler() {
        return outputFluidHandler;
    }

    public ResourceHandler<FluidResource> getFluidCapability() {
        return outputFluidHandler;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, Inventory inventory, Player player) {
        return new ControllerMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.casting.controller");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {

        inputHandler.serialize(output.child("inputs"));
        outputFluidHandler.serialize(output.child("outputFluids"));
        output.putIntArray("progress", progress);
        output.putIntArray("maxProgress", maxProgress);

        super.saveAdditional(output);
    }


    @Override
    protected void loadAdditional(ValueInput input) {
        inputHandler.deserialize(input.childOrEmpty("inputs"));
        outputFluidHandler.deserialize(input.childOrEmpty("outputFluids"));

        int[] loadedProgress = input.getIntArray("progress").orElse(new int[15]);
        int[] loadedMaxProgress = input.getIntArray("maxProgress").orElse(new int[15]);
        this.progress = loadedProgress.length >= 15 ? loadedProgress : new int[15];
        this.maxProgress = loadedMaxProgress.length >= 15 ? loadedMaxProgress : new int[15];

        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        dropInventoryContents(inputHandler);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(outputFluidHandler));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            System.out.println(component.fluids());
            component.applyToHandlers(outputFluidHandler);
        }
    }
}