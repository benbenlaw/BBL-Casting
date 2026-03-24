package com.benbenlaw.casting.data.custom;

import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.Arrays;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class FluidStackTemplateHelper {

    public static FluidStackTemplate getFluidStack(String string, int amount) {
        return new FluidStackTemplate(FLUIDS_MAP.get(string).getFluid(), amount);
    }

    public static NonNullList<FluidStackTemplate> fluidList(FluidStackTemplate... stacks) {
        NonNullList<FluidStackTemplate> list = NonNullList.create();
        list.addAll(Arrays.asList(stacks));
        return list;
    }
}
