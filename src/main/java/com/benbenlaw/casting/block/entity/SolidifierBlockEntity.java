package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.SolidifierBlock;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import org.jspecify.annotations.NonNull;

import java.util.OptionalInt;

public class SolidifierBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final ContainerData data;
    private int maxProgress = 200;
    private int progress = 0;
    private OptionalInt temperature = OptionalInt.empty();

    private final InputItemHandler inputHandler = new InputItemHandler(this, 1, (i, stack) -> i == 0);
    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 1, 16000, (i, stack) -> i == 0);
    private final OutputItemHandler outputHandler = new OutputItemHandler(this, 1, i -> i == 0);

    public SolidifierBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.SOLIDIFIER_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SolidifierBlockEntity.this.progress;
                    case 1 -> SolidifierBlockEntity.this.maxProgress;
                    case 2 -> SolidifierBlockEntity.this.temperature.orElse(Integer.MIN_VALUE);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SolidifierBlockEntity.this.progress = value;
                    case 1 -> SolidifierBlockEntity.this.maxProgress = value;
                    case 2 -> SolidifierBlockEntity.this.temperature =
                            (value == Integer.MIN_VALUE) ? OptionalInt.empty() : OptionalInt.of(value);
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        boolean isRunning = level.getBlockState(worldPosition).getValue(SolidifierBlock.RUNNING);

        TankBlockEntity activeFuelTank = getActiveFuelTank(level, worldPosition);
        int currentTemp = activeFuelTank != null ? activeFuelTank.getFuelTemp().orElse(20) : 20;
        this.temperature = activeFuelTank != null ? OptionalInt.of(currentTemp) : OptionalInt.empty();

        if (!isRunning) {
            updateWorkingState(false);
            if (progress > 0) {
                progress = 0;
                setChanged();
                sync();
            }
            return;
        }

        boolean changed = false;
        boolean isCurrentlyWorking = false;

        ItemStack inputStack = ItemUtil.getStack(inputHandler, 0);
        RecipeHolder<SolidifierRecipe> recipeHolder = getRecipe();

        if (canFillBucket(inputStack)) {
            isCurrentlyWorking = true;

            maxProgress = 20;

            progress++;
            changed = true;

            if (progress >= maxProgress) {
                executeBucketFill();
                progress = 0;
            }
        }

        else if (recipeHolder != null) {
            SolidifierRecipe recipe = recipeHolder.value();

            if (canFormOutput(recipe) && hasEnoughFluid(recipe)) {
                isCurrentlyWorking = true;

                int baseMaxProgress = 200;
                double recipeModifier = recipe.durationModifier().orElse(1.0);
                double finalModifier = recipeModifier;

                if (activeFuelTank != null) {
                    int fluidTemp = recipe.meltingTemp();
                    if (currentTemp < fluidTemp) {
                        int tempDifference = fluidTemp - currentTemp;
                        float tempModifier = (tempDifference / 25f) * 0.01f;
                        finalModifier = recipeModifier - tempModifier;
                    }
                }

                maxProgress = (int) (baseMaxProgress * finalModifier);
                if (maxProgress < 10) maxProgress = 10;

                progress++;
                changed = true;

                if (progress >= maxProgress) {
                    executeSolidifying(recipe, activeFuelTank);
                    progress = 0;
                }
            } else if (progress > 0) {
                progress = 0;
                changed = true;
            }
        } else if (progress > 0) {
            progress = 0;
            changed = true;
        }

        updateWorkingState(isCurrentlyWorking);

        if (changed) {
            setChanged();
            sync();
        }
    }

    private boolean canFillBucket(ItemStack inputStack) {
        if (!inputStack.is(net.minecraft.world.item.Items.BUCKET)) return false;

        FluidStack fluidInTank = FluidUtil.getStack(inputFluidHandler, 0);
        if (fluidInTank.getAmount() < 1000) return false;

        ItemStack fullBucket = new ItemStack(fluidInTank.getFluid().getBucket());
        if (fullBucket.is(net.minecraft.world.item.Items.AIR)) return false;

        try (Transaction tx = Transaction.open(null)) {
            long inserted = outputHandler.insertInternalReturn(0, ItemResource.of(fullBucket), 1, tx);
            return inserted == 1;
        }
    }

    private void executeBucketFill() {
        FluidStack fluidInTank = FluidUtil.getStack(inputFluidHandler, 0);
        ItemStack fullBucket = new ItemStack(fluidInTank.getFluid().getBucket());

        try (Transaction tx = Transaction.open(null)) {
            inputFluidHandler.extractInternal(0, FluidResource.of(fluidInTank), 1000, tx);
            inputHandler.extractInternal(0, ItemResource.of(new ItemStack(Items.BUCKET)), 1, tx);
            outputHandler.insertInternal(0, ItemResource.of(fullBucket), 1, tx);
            tx.commit();
        }
    }

    public static @Nullable TankBlockEntity getActiveFuelTank(Level level, BlockPos worldPosition) {
        if (level == null) return null;
        for (var dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof TankBlockEntity tank) {
                if (!tank.getInputFluidHandler().getResource(0).isEmpty()) {
                    return tank;
                }
            }
        }
        return null;
    }

    private void executeSolidifying(SolidifierRecipe recipe, @Nullable TankBlockEntity fuelTank) {
        try (Transaction tx = Transaction.open(null)) {
            inputFluidHandler.extractInternal(0, FluidResource.of(recipe.fluid()), recipe.fluid().amount(), tx);

            if (fuelTank != null) {
                int currentTemp = fuelTank.getFuelTemp().orElse(20);
                int fluidTemp = recipe.meltingTemp();

                if (currentTemp < fluidTemp) {
                    FluidStack fuelStack = FluidUtil.getStack(fuelTank.getInputFluidHandler(), 0);
                    if (!fuelStack.isEmpty()) {
                        var fuelRecipe = TankBlockEntity.getFuel(level, fuelStack);
                        if (fuelRecipe != null) {
                            fuelTank.getInputFluidHandler().extractInternal(
                                    0,
                                    fuelTank.getInputFluidHandler().getResource(0),
                                    fuelRecipe.value().fluid().amount(),
                                    tx
                            );
                        }
                    }
                }
            }

            ItemStack result = getStackFromSized(recipe.output());
            if (!result.isEmpty()) {
                outputHandler.insertInternal(0, ItemResource.of(result), result.getCount(), tx);
            }

            tx.commit();
        }
    }

    private void updateWorkingState(boolean working) {
        assert level != null;
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
                    0, ItemResource.of(recipeOutput), recipeOutput.getCount(), tx);
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

    @Override
    protected void saveAdditional(ValueOutput output) {
        inputHandler.serialize(output.child("input"));
        inputFluidHandler.serialize(output.child("inputFluid"));
        outputHandler.serialize(output.child("output"));
        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);

        output.putInt("temperature", temperature.orElse(Integer.MIN_VALUE));

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        inputHandler.deserialize(input.childOrEmpty("input"));
        inputFluidHandler.deserialize(input.childOrEmpty("inputFluid"));
        outputHandler.deserialize(input.childOrEmpty("output"));
        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 200);

        int tempVal = input.getIntOr("temperature", Integer.MIN_VALUE);
        this.temperature = (tempVal == Integer.MIN_VALUE) ? OptionalInt.empty() : OptionalInt.of(tempVal);

        super.loadAdditional(input);
    }

    public InputItemHandler getInputHandler() { return inputHandler; }
    public InputFluidHandler getInputFluidHandler() { return inputFluidHandler; }
    public OutputItemHandler getOutputHandler() { return outputHandler; }
    public ResourceHandler<ItemResource> getItemCapability() { return new CombinedItemHandler(inputHandler, outputHandler); }
    public ResourceHandler<FluidResource> getFluidCapability() { return inputFluidHandler; }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, @NonNull Inventory inventory, @NonNull Player player) {
        return new SolidifierMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.casting.solidifier");
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
        dropInventoryContents(inputHandler);
        dropInventoryContents(outputHandler);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(inputFluidHandler));
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            component.applyToHandlers(inputFluidHandler);
        }
    }
}