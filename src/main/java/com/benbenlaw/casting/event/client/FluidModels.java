package com.benbenlaw.casting.event.client;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.fluid.FluidRegistryObject;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import org.jspecify.annotations.NonNull;

import java.util.List;

@EventBusSubscriber(modid = Casting.MOD_ID, value = Dist.CLIENT)
public class FluidModels {

    @SubscribeEvent
    private static void registerFluidModels(RegisterFluidModelsEvent event) {

        for (FluidData data : FluidData.FLUID_DEFINITIONS) {

            var fluidObject = CastingFluids.FLUIDS_MAP.get(data.name());
            var still = new Material(Core.identifier(data.stillTexture()));

            var flowing = new Material(Core.identifier(data.flowTexture()));

            FluidModel.Unbaked model = new FluidModel.Unbaked(
                    still,
                    flowing,
                    null,
                    state -> data.tint(),
                    null
            );

            event.register(model,
                    fluidObject.getStillFluid(),
                    fluidObject.getFlowingFluid()
            );
        }
    }
}
