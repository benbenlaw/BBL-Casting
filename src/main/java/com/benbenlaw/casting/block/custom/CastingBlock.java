package com.benbenlaw.casting.block.custom;

import com.benbenlaw.core.block.SyncableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class CastingBlock extends SyncableBlock {

    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    public CastingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(RUNNING, true)
                .setValue(WORKING, false));
    }

    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CastingBlock::new);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(RUNNING, true)
                .setValue(WORKING, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WORKING);
    }

    public static void setWorkingState(Level level, BlockPos pos, boolean working) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(WORKING) && state.getValue(WORKING) != working) {
            level.setBlockAndUpdate(pos, state.setValue(WORKING, working));
        }
    }
}