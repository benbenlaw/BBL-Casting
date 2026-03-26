package com.benbenlaw.casting.data.custom;

import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.Arrays;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class FluidStackTemplateHelper {

    public static FluidStackTemplate getFluidStack(String string, int amount) {
        return new FluidStackTemplate(FLUIDS_MAP.get(string).getFluid(), amount);
    }

    public static SizedFluidIngredient getFluidIngredient(String string, int amount) {
        return new SizedFluidIngredient(FluidIngredient.of(FLUIDS_MAP.get(string).getFluid()), amount);
    }

    public static SizedFluidIngredient getFluidTagIngredient(HolderSet<Fluid> fluidSet, int amount) {
        return new SizedFluidIngredient(FluidIngredient.of(fluidSet), amount);
    }



    public static NonNullList<FluidStackTemplate> fluidList(FluidStackTemplate... stacks) {
        NonNullList<FluidStackTemplate> list = NonNullList.create();
        list.addAll(Arrays.asList(stacks));
        return list;
    }
}
