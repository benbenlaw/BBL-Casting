package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SolidifierMenu extends SimpleAbstractContainerMenu {

    protected SolidifierBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public SolidifierMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(4));
    }

    public SolidifierMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CastingMenuTypes.SOLIDIFIER_MENU.get(), containerID, inventory, blockPos, 2);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (SolidifierBlockEntity) this.level.getBlockEntity(blockPos);

        assert blockEntity != null;
        this.addSlot(new InputSlot(blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, 0, 44, 35) {
            @Override
            public int getMaxStackSize(ItemStack stack) {
                int maxStackSize = 64;
                if (stack.is(CastingTags.Items.MOLDS)) {
                    maxStackSize = 1;
                }
                return maxStackSize;
            }
        });

        this.addSlot(new ResultSlot(blockEntity.getOutputHandler(), blockEntity.getOutputHandler()::set, 0, 116, 35));

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {

        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
