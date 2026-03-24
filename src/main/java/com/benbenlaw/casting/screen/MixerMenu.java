package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.block.entity.MixerBlockEntity;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;

public class MixerMenu extends SimpleAbstractContainerMenu {

    protected MixerBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public MixerMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(2));
    }

    public MixerMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CastingMenuTypes.MIXER_MENU.get(), containerID, inventory, blockPos, 0);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (MixerBlockEntity) this.level.getBlockEntity(blockPos);

        this.addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {

        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24; // This is the height/width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
