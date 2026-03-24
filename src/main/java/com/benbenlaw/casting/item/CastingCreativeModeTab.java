package com.benbenlaw.casting.item;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.fluid.CastingFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CastingCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Casting.MOD_ID);

    public static final Supplier<CreativeModeTab> CASTING_TAB = CREATIVE_MODE_TABS.register("casting", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CastingItems.INGOT_MOLD.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.casting"))
            .displayItems(CastingItems.ITEMS.getEntries()).build());
}


