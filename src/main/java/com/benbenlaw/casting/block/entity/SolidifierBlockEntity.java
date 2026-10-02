package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.SolidifierBlock;
import com.benbenlaw.casting.config.CastingConfig;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.FluidMoverItem;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import com.benbenlaw.casting.screen.SolidifierMenu;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.FilterFluidHandler;
import com.benbenlaw.core.block.entity.handler.fluid.SyncableFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SolidifierBlockEntity extends SyncableBlockEntity implements MenuProvider, FluidAccepting {

    private final ContainerData data;
    private int maxProgress = CastingConfig.defaultSolidifierSpeed.get();
    private int progress = 0;
    private OptionalInt temperature = OptionalInt.empty();

    private List<RecipeHolder<SolidifierRecipe>> cachedMoldCandidates = List.of();
    private boolean moldCacheDirty = true;
    private RecipeManager lastRecipeManager = null;

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, 2, (i, stack) -> i == 0, i -> i == 1) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            if (index == INPUT_SLOT) {
                moldCacheDirty = true;
            }
            super.onContentsChanged(index, previousContents);
        }
    };

    private final SyncableFluidHandler fluidInventory = new SyncableFluidHandler(this, 1, 8000, (i, stack) -> i == 0, i -> i == 0);
    private final SyncableItemHandler storedMolds = new SyncableItemHandler(this, 20, (i, stack) -> false, i -> false);
    private FilterFluidHandler filterFluidHandler = new FilterFluidHandler(this, 1);
    public FluidStack fuelStack = FluidStack.EMPTY;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

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

        boolean progressChanged = false;
        boolean contentsChanged = false;
        boolean isCurrentlyWorking = false;

        ItemStack inputStack = ItemUtil.getStack(inventory, INPUT_SLOT);
        RecipeHolder<SolidifierRecipe> recipeHolder = getRecipe();

        if (canFillBucket(inputStack)) {
            isCurrentlyWorking = true;
            maxProgress = 20;
            progress++;
            progressChanged = true;

            if (progress >= maxProgress) {
                executeBucketFill();
                progress = 0;
                contentsChanged = true;
            }
        }

        else if (recipeHolder != null) {
            SolidifierRecipe recipe = recipeHolder.value();

            if (canFormOutput(recipe) && hasEnoughFluid(recipe)) {
                isCurrentlyWorking = true;

                int baseMaxProgress = CastingConfig.defaultSolidifierSpeed.get();
                boolean fuelBenefited = activeFuelTank != null && currentTemp < recipe.meltingTemp();
                double finalModifier = getFinalModifier(recipe, activeFuelTank, currentTemp);

                maxProgress = (int) (baseMaxProgress * finalModifier);
                if (maxProgress < 5) maxProgress = 5;

                FluidStack coolantStack = fuelBenefited
                        ? FluidUtil.getStack(activeFuelTank.getFluidHandler(), 0)
                        : FluidStack.EMPTY;

                boolean coolantEmpty = fuelBenefited && coolantStack.isEmpty();

                if (coolantEmpty) {
                    if (progress > 0) {
                        progress = 0;
                        progressChanged = true;
                    }
                } else {
                    if (progress < maxProgress) {
                        progress++;
                        progressChanged = true;
                    }

                    if (progress >= maxProgress) {
                        if (!fuelBenefited) {
                            executeSolidifying(recipe, null, false);
                            progress = 0;
                            contentsChanged = true;
                        } else {
                            var fuelRecipeHolder = TankBlockEntity.getFuel(level, coolantStack);
                            int requiredAmount = fuelRecipeHolder != null
                                    ? fuelRecipeHolder.value().fluid().amount()
                                    : 100;

                            if (coolantStack.getAmount() >= requiredAmount) {
                                executeSolidifying(recipe, activeFuelTank, true);
                                progress = 0;
                                contentsChanged = true;
                            }
                            // else: coolant present but below required amount — hold at maxProgress
                        }
                    }
                }
            } else if (progress > 0) {
                progress = 0;
                progressChanged = true;
            }
        } else if (progress > 0) {
            progress = 0;
            progressChanged = true;
        }

        updateWorkingState(isCurrentlyWorking);

        if (progressChanged || contentsChanged) {
            setChanged();
        }
        if (contentsChanged) {
            sync();
        }
    }

    private static double getFinalModifier(SolidifierRecipe recipe, TankBlockEntity activeFuelTank, int currentTemp) {
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
        return finalModifier;
    }

    private boolean canFillBucket(ItemStack inputStack) {
        if (!inputStack.is(Items.BUCKET)) return false;

        FluidStack fluidInTank = FluidUtil.getStack(fluidInventory, 0);
        if (fluidInTank.getAmount() < 1000) return false;

        ItemStack fullBucket = new ItemStack(fluidInTank.getFluid().getBucket());
        if (fullBucket.is(Items.AIR)) return false;

        return inventory.runInternal(() -> {
            try (Transaction tx = Transaction.openRoot()) {
                long inserted = inventory.insert(
                        OUTPUT_SLOT,
                        ItemResource.of(fullBucket),
                        1,
                        tx
                );
                return inserted == 1;
            }
        });
    }

    private void executeBucketFill() {
        FluidStack fluidInTank = FluidUtil.getStack(fluidInventory, 0);
        ItemStack fullBucket = new ItemStack(fluidInTank.getFluid().getBucket());

        try (Transaction tx = Transaction.open(null)) {
            inventory.runInternal(() -> {
                inventory.extract(INPUT_SLOT, ItemResource.of(new ItemStack(Items.BUCKET)), 1, tx);
                inventory.insert(OUTPUT_SLOT, ItemResource.of(fullBucket), 1, tx);
            });

            fluidInventory.runInternal(() -> {
                fluidInventory.extract(0, FluidResource.of(fluidInTank), 1000, tx);
            });

            tx.commit();
        }
    }

    public static @Nullable TankBlockEntity getActiveFuelTank(Level level, BlockPos worldPosition) {
        if (level == null) return null;
        for (var dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof TankBlockEntity tank) {
                if (!tank.getFluidHandler().getResource(0).isEmpty() && tank.getFuelTemp().isPresent()) {
                    return tank;
                }
            }
        }
        return null;
    }

    public static @Nullable TankBlockEntity getAdjacentFuelTank(Level level, BlockPos worldPosition) {
        if (level == null) return null;
        for (var dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof TankBlockEntity tank) {
                if (!tank.getFluidHandler().getResource(0).isEmpty()) {
                    return tank;
                }
            }
        }
        return null;
    }

    private void executeSolidifying(SolidifierRecipe recipe, @Nullable TankBlockEntity fuelTank, boolean consumeFuel) {
        boolean fuelDrained = false;

        try (Transaction tx = Transaction.open(null)) {
            FluidStack inTank = FluidUtil.getStack(fluidInventory, 0);

            if (!ItemUtil.getStack(inventory, INPUT_SLOT).is(CastingTags.Items.MOLDS)) {
                inventory.runInternal(() -> {
                    inventory.extract(INPUT_SLOT, inventory.getResource(INPUT_SLOT), recipe.mold().count(), tx);
                });
            }

            fluidInventory.runInternal(() -> {
                fluidInventory.extract(0, FluidResource.of(inTank), recipe.fluid().amount(), tx);
            });

            if (consumeFuel && fuelTank != null) {
                SyncableFluidHandler fuelHandler = (SyncableFluidHandler) fuelTank.getFluidHandler();
                FluidStack fuelInTank = FluidUtil.getStack(fuelHandler, 0);
                var fuelRecipeHolder = TankBlockEntity.getFuel(level, fuelInTank);

                if (fuelRecipeHolder != null) {
                    int amountToDrain = fuelRecipeHolder.value().fluid().amount();
                    fuelHandler.runInternal(() -> {
                        fuelHandler.extract(0, FluidResource.of(fuelInTank), amountToDrain, tx);
                    });
                    fuelDrained = true;
                }
            }

            ItemStack result = getStackFromSized(recipe.output());
            if (!result.isEmpty()) {
                inventory.runInternal(() -> {
                    inventory.insert(OUTPUT_SLOT, ItemResource.of(result), result.getCount(), tx);
                });
            }
            tx.commit();
        }

        if (fuelDrained) {
            fuelTank.setChanged();
            fuelTank.sync();
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
        FluidStack inTank = FluidUtil.getStack(fluidInventory, 0);
        return !inTank.isEmpty() &&
                recipe.fluid().ingredient().test(inTank) &&
                inTank.getAmount() >= recipe.fluid().amount();
    }

    private boolean canFormOutput(SolidifierRecipe recipe) {
        ItemStack recipeOutput = getStackFromSized(recipe.output());
        if (recipeOutput.isEmpty()) return false;

        return inventory.runInternal(() -> {
            try (Transaction tx = Transaction.openRoot()) {
                long inserted = inventory.insert(
                        OUTPUT_SLOT,
                        ItemResource.of(recipeOutput),
                        recipeOutput.getCount(),
                        tx
                );
                return inserted == recipeOutput.getCount();
            }
        });
    }

    private ItemStack getStackFromSized(SizedIngredient sizedIngredient) {
        DataComponentPatch components = sizedIngredient.ingredient().getCustomIngredient() instanceof DataComponentIngredient componentIngredient
                ? componentIngredient.components()
                : DataComponentPatch.EMPTY;

        return sizedIngredient.ingredient().items()
                .findFirst()
                .map(holder -> new ItemStackTemplate(holder, sizedIngredient.count(), components).create())
                .orElse(ItemStack.EMPTY);
    }

    private RecipeHolder<SolidifierRecipe> getRecipe() {
        if (level == null || level.getServer() == null) return null;

        ItemStack mold = ItemUtil.getStack(inventory, INPUT_SLOT);
        FluidStack fluid = FluidUtil.getStack(fluidInventory, 0);

        if (mold.isEmpty() || fluid.isEmpty()) return null;

        RecipeManager recipeManager = level.getServer().getRecipeManager();
        if (moldCacheDirty || recipeManager != lastRecipeManager) {
            List<RecipeHolder<SolidifierRecipe>> candidates = new ArrayList<>();
            for (var holder : recipeManager.recipeMap().byType(SolidifierRecipe.TYPE)) {
                if (holder.value().mold().test(mold)) {
                    candidates.add(holder);
                }
            }
            cachedMoldCandidates = candidates;
            moldCacheDirty = false;
            lastRecipeManager = recipeManager;
        }

        for (var holder : cachedMoldCandidates) {
            SolidifierRecipe recipe = holder.value();
            if (recipe.fluid().ingredient().test(fluid) && fluid.getAmount() >= recipe.fluid().amount()) {
                return holder;
            }
        }
        return null;
    }
    public boolean onPlayerUse(Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof FluidMoverItem) {
            return FluidMoverItem.onBlockInteract(stack, fluidInventory, new int[]{0}, new int[]{0});
        }

        if (stack.is(CastingTags.Items.MOLDS)) {

            for (int i = 0; i < storedMolds.size(); i++) {
                ItemStack existing = ItemUtil.getStack(storedMolds, i);

                if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)) {
                    return false;
                }
            }

            for (int i = 0; i < storedMolds.size(); i++) {
                if (storedMolds.getResource(i).isEmpty()) {

                    int slot = i;

                    storedMolds.runInternal(() -> {
                        try (Transaction tx = Transaction.open(null)) {

                            int inserted = storedMolds.insert(slot, ItemResource.of(stack), 1, tx);

                            if (inserted > 0) {
                                stack.shrink(1);
                                tx.commit();
                                return true;
                            }

                            return false;
                        }
                    });

                    return true;
                }
            }

            return true;
        }

        if (player.isCrouching() && stack.isEmpty()) {
            for (int i = 0; i < storedMolds.size(); i++) {
                if (!storedMolds.getResource(i).isEmpty()) {
                    int slot = i;
                    storedMolds.runInternal(() -> {
                        try (Transaction tx = Transaction.open(null)) {
                            ItemStack stored = ItemUtil.getStack(storedMolds, slot);
                            if (stored.isEmpty()) {
                                return false;
                            }
                            int extracted = storedMolds.extract(slot, ItemResource.of(stored), 1, tx);
                            if (extracted > 0) {
                                player.addItem(stored.copyWithCount(1));
                                tx.commit();
                                return true;
                            }
                            return false;
                        }
                    });

                    return true;
                }
            }

            return true;
        }

        try (Transaction tx = Transaction.open(null)) {
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, fluidInventory, tx);
            if (result) {
                tx.commit();
            }
            return result;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        inventory.serialize(output.child("inventory"));
        fluidInventory.serialize(output.child("fluidInventory"));
        filterFluidHandler.serialize(output.child("filterFluid"));
        storedMolds.serialize(output.child("storedMolds"));
        output.store("fluid", FluidStack.OPTIONAL_CODEC, fuelStack);
        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);

        output.putInt("temperature", temperature.orElse(Integer.MIN_VALUE));

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        inventory.deserialize(input.childOrEmpty("inventory"));
        fluidInventory.deserialize(input.childOrEmpty("fluidInventory"));
        filterFluidHandler.deserialize(input.childOrEmpty("filterFluid"));
        storedMolds.deserialize(input.childOrEmpty("storedMolds"));
        fuelStack = input.read("fluid", FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY);
        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", CastingConfig.defaultSolidifierSpeed.get());

        int tempVal = input.getIntOr("temperature", Integer.MIN_VALUE);
        this.temperature = (tempVal == Integer.MIN_VALUE) ? OptionalInt.empty() : OptionalInt.of(tempVal);

        super.loadAdditional(input);
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    public FluidStacksResourceHandler getFluidHandler() {
        return fluidInventory;
    }

    public FilterFluidHandler getFilterFluidHandler() { return filterFluidHandler; }
    public SyncableItemHandler getStoredMolds() { return storedMolds; }

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
        dropInventoryContents(inventory);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(fluidInventory));

        NonNullList<ItemStack> items = storedMolds.copyToList();
        NonNullList<ItemStack> filledItems = NonNullList.create();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                ItemStack copy = stack.copy();
                filledItems.add(copy);
            }
        }

        builder.set(CastingDataComponents.STORED_MOLDS.get(), filledItems);
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            component.applyToHandlers(fluidInventory);
        }
        List<ItemStack> molds = components.get(CastingDataComponents.STORED_MOLDS.get());
        if (molds != null) {
            molds.forEach(stack -> {
                for (int i = 0; i < storedMolds.size(); i++) {
                    if (storedMolds.getResource(i).isEmpty()) {
                        storedMolds.set(i, ItemResource.of(stack), stack.count());
                        break;
                    }
                }
            });
        }
    }

    @Override
    public SyncableFluidHandler receivingHandler() {
        return fluidInventory;
    }

    @Override
    public int[] acceptingTanks() {
        return new int[]{0};
    }


    @Override
    public FilterFluidHandler getFilter() {
        return filterFluidHandler;
    }
}