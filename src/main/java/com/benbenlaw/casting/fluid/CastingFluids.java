package com.benbenlaw.casting.fluid;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.core.fluid.FluidDeferredRegister;
import com.benbenlaw.core.fluid.FluidRegistryObject;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.HashMap;
import java.util.Map;

public class CastingFluids {

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Casting.MOD_ID);

    public static final Map<String, FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes,
                BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem>> FLUIDS_MAP = new HashMap<>();

    static {

        for (FluidData data : FluidData.FLUID_DEFINITIONS) {

            var fluid = FLUIDS.register(data.name(), (renderProperties) ->
                    renderProperties.texture(
                            Identifier.fromNamespaceAndPath(Casting.MOD_ID, data.stillTexture()),
                            Identifier.fromNamespaceAndPath(Casting.MOD_ID, data.flowTexture())
                    ).tint(data.tint())
            );

            FLUIDS_MAP.put(data.name(), fluid);
        }
    }

}
