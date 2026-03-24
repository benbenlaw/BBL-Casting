package com.benbenlaw.casting.util;

import com.benbenlaw.core.fluid.FluidRegistryObject;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class CastingColorHandler {

    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.ItemTintSources event) {

        // Get all the bucket items from the fluids map
        var buckets = FLUIDS_MAP.values().stream()
                .map(FluidRegistryObject::getBucket)
                .toArray(Item[]::new);

        //event.register((stack, tint) -> {
        //    var fluid = ((BucketItem) stack.getItem()).content;
        //    return tint == 1 ? IClientFluidTypeExtensions.of(fluid).getTintColor() : -1;
        //}, buckets);
    }

}
