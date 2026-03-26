package com.benbenlaw.casting.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.neoforged.neoforge.fluids.FluidStack;

public class TankBlockEntityRenderState extends BlockEntityRenderState {
    public FluidStack fluidStack;
    public int tankCapacity;
}
