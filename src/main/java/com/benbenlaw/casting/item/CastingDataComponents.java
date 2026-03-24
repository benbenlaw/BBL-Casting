package com.benbenlaw.casting.item;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.item.util.FluidListComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CastingDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Casting.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidListComponent>> FLUIDS =
            COMPONENTS.register("fluids", () ->
                    DataComponentType.<FluidListComponent>builder()
                            .persistent(FluidListComponent.CODEC)
                            .networkSynchronized(FluidListComponent.STREAM_CODEC)
                            .cacheEncoding()
                            .build());


}
