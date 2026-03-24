package com.benbenlaw.casting.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.TankBlock;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class TankBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 1, 16000, (i, stack) -> i == 0);

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(CastingBlockEntities.FUEL_TANK_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        assert level != null;
        if (!level.isClientSide()) {

            if (!level.getBlockState(worldPosition).getValue(TankBlock.RUNNING)) return;
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, Inventory inventory, Player player) {
        return null; //new SolidifierMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.casting.tank");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {

        inputFluidHandler.serialize(output);

        super.saveAdditional(output);
    }


    @Override
    protected void loadAdditional(ValueInput input) {

        inputFluidHandler.deserialize(input);

        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {

    }
}