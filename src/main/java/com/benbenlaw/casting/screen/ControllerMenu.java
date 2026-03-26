package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.block.entity.ControllerBlockEntity;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;

public class ControllerMenu extends SimpleAbstractContainerMenu {

    protected ControllerBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public ControllerMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(31));
    }
    public ControllerMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CastingMenuTypes.CONTROLLER_MENU.get(), containerID, inventory, blockPos, 15);

        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (ControllerBlockEntity) this.level.getBlockEntity(blockPos);

        assert blockEntity != null;
        for (int i = 0; i < 15; i++) {
            int row = i / 5;
            int col = i % 5;

            int slotX = 8 + (col * 19);
            int slotY = 16 + (row * 19);

            this.addSlot(new InputSlot(blockEntity.getInputHandler(),
                    blockEntity.getInputHandler()::set, i, slotX, slotY));
        }

        addDataSlots(data);
    }

    public boolean isCrafting() {
        for (int i = 0; i < 15; i++) {
            if (data.get(i) > 0) {
                return true;
            }
        }
        return false;
    }

    public int getScaledProgress(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 15) return 0;

        int progress = this.data.get(slotIndex);
        int maxProgress = this.data.get(slotIndex + 15);
        int progressBarHeight = 16;

        if (maxProgress == 0 || progress == 0) return 0;
        return (int) Math.ceil((double) progress * progressBarHeight / maxProgress);
    }

}