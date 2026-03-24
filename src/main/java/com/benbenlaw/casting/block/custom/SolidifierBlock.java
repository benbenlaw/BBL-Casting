package com.benbenlaw.casting.block.custom;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.core.block.SyncableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolidifierBlock extends CastingBlock {

    public static final MapCodec<SolidifierBlock> CODEC = simpleCodec(SolidifierBlock::new);

    @Override
    protected @NotNull MapCodec<SolidifierBlock> codec() {
        return CODEC;
    }

    public SolidifierBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof SolidifierBlockEntity entity1) {
                if (entity1.onPlayerUse(player, player.getUsedItemHand())) {
                    return InteractionResult.SUCCESS;
                }
                player.openMenu(new SimpleMenuProvider(entity1, entity1.getDisplayName()), pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SolidifierBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, CastingBlockEntities.SOLIDIFIER_BLOCK_ENTITY.get(),
                (thisLevel, thisPos, thisState, thisEntity) -> thisEntity.tick());
    }
}
