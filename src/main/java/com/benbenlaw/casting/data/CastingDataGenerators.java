package com.benbenlaw.casting.data;


import com.benbenlaw.casting.Casting;
import com.ibm.icu.impl.locale.AsciiUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.lwjgl.system.macosx.CGEventTapCallBack;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Casting.MOD_ID)
public class CastingDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new CastingBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new CastingItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new CastingFluidTags(packOutput, lookupProvider));
        generator.addProvider(true, new CastingLangProvider(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(CastingLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(true, new CastingModelProvider(packOutput));
        generator.addProvider(true, new CastingRecipeProvider.Runner(packOutput, lookupProvider));



    }


}
