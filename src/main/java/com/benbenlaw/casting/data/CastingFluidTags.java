package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.casting.data.custom.FluidStackTemplateHelper.getFluidStack;
import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class CastingFluidTags extends FluidTagsProvider {


    public CastingFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Casting.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Fluids
        for (var entry : FLUIDS_MAP.entrySet()) {

            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", entry.getKey()));

            tag(tag).add(entry.getValue().getFluid());
            tag(tag).add(entry.getValue().getFlowingFluid());
        }

        tag(Tags.Fluids.EXPERIENCE).add(getFluidStack("molten_experience", 1).fluid().value());
    }
}
