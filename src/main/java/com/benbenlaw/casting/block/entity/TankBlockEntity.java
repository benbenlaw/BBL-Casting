package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.TankBlock;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.custom.FuelRecipe;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.NonNull;

import java.util.OptionalInt;

public class TankBlockEntity extends SyncableBlockEntity{

    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 1, 16000, (i, stack) -> i == 0);

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.TANK_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        assert level != null;
        if (!level.isClientSide()) {
            if (!level.getBlockState(worldPosition).getValue(TankBlock.RUNNING)) return;
        }
    }

    public OptionalInt getFuelTemp() {
        FluidStack stack = FluidUtil.getStack(inputFluidHandler, 0);
        if (stack.isEmpty()) return OptionalInt.empty();

        RecipeHolder<FuelRecipe> fuelRecipe = getFuel(level, stack);
        if (fuelRecipe == null) return OptionalInt.empty();

        return OptionalInt.of(fuelRecipe.value().temp());
    }

    public InputFluidHandler getInputFluidHandler() {
        return inputFluidHandler;
    }

    public static RecipeHolder<FuelRecipe> getFuel(Level level, FluidStack stack) {
        if (level == null || level.getServer() == null || stack.isEmpty()) return null;

        return level.getServer().getRecipeManager()
                .recipeMap()
                .values()
                .stream()
                .filter(holder -> holder.value().getType() == FuelRecipe.TYPE)
                .map(holder -> (RecipeHolder<FuelRecipe>) holder)
                .filter(holder -> FluidStack.isSameFluid(holder.value().fluid().create(), stack))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        inputFluidHandler.serialize(output.child("tankContent"));
        super.saveAdditional(output);
    }


    @Override
    protected void loadAdditional(ValueInput input) {
        inputFluidHandler.deserialize(input.childOrEmpty("tankContent"));
        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {

    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, inputFluidHandler);
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