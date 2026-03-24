package com.benbenlaw.casting.data.custom;

import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Arrays;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class FluidStackHelper {

    public static FluidStack getFluidStack(String string, int amount) {
        return new FluidStack(FLUIDS_MAP.get(string).getFluid(), amount);
    }

    public static NonNullList<FluidStack> fluidList(FluidStack... stacks) {
        NonNullList<FluidStack> list = NonNullList.create();
        list.addAll(Arrays.asList(stacks));
        return list;
    }
}
