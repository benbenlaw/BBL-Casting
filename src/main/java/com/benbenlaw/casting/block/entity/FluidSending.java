package com.benbenlaw.casting.block.entity;

import com.benbenlaw.core.block.entity.handler.fluid.OutputFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface FluidSending {

    OutputFluidHandler sendingHandler();

    default void tickResourceSending(Level level, BlockPos pos) {
        if (level == null || level.isClientSide()) return;

        OutputFluidHandler myHandler = sendingHandler();

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockEntity neighbourBlockEntity = level.getBlockEntity(neighborPos);

            if (neighbourBlockEntity instanceof FluidAccepting accepting) {
                var neighborInput = accepting.receivingHandler();
                var neighborFilter = accepting.getFilter();

                if (neighborInput != null) {
                    for (int i = 0; i < myHandler.size(); i++) {

                        while (true) {
                            FluidStack myStack = FluidUtil.getStack(myHandler, i);
                            if (myStack.isEmpty()) break;

                            long totalMoved = 0;

                            try (Transaction tx = Transaction.open(null)) {
                                for (int j = 0; j < neighborInput.size(); j++) {
                                    if (neighborFilter != null) {
                                        FluidStack filterStack = FluidUtil.getStack(neighborFilter, j);
                                        if (!filterStack.isEmpty() && !FluidStack.isSameFluidSameComponents(myStack, filterStack)) {
                                            continue;
                                        }
                                    }

                                    long moved = neighborInput.insert(j, FluidResource.of(myStack), myStack.getAmount(), tx);

                                    if (moved > 0) {
                                        myHandler.extract(i, FluidResource.of(myStack), (int) moved, tx);
                                        totalMoved = moved;
                                        break;
                                    }
                                }

                                if (totalMoved > 0) {
                                    tx.commit();
                                } else {
                                    break; // No valid slots found for this fluid in this neighbor
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
