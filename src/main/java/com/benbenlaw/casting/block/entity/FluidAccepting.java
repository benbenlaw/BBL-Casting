package com.benbenlaw.casting.block.entity;

import com.benbenlaw.core.block.entity.handler.fluid.FilterFluidHandler;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;

import javax.annotation.Nullable;

public interface FluidAccepting {

    InputFluidHandler receivingHandler();

    default @Nullable FilterFluidHandler getFilter() {
        return null;
    }

}
