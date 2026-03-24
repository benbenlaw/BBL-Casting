package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.casting.util.CastingTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class CastingItemTags extends ItemTagsProvider {

    public CastingItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Casting.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Ball Items
        tag(CastingTags.Items.BALL_ITEMS)
                .add(Items.SNOWBALL)
                .add(Items.SLIME_BALL)
                .add(Items.MAGMA_CREAM)
                .add(Items.FIRE_CHARGE)
                .add(Items.ENDER_PEARL)
                .add(Items.ENDER_EYE)
                .add(Items.CLAY_BALL);

        //Bricks
        tag(Tags.Items.BRICKS).add(CastingItems.BLACK_BRICK.asItem());

        //Molds
        tag(CastingTags.Items.MOLDS)
                .add(CastingItems.BLOCK_MOLD.asItem())
                .add(CastingItems.DUST_MOLD.asItem())
                .add(CastingItems.GEAR_MOLD.asItem())
                .add(CastingItems.INGOT_MOLD.asItem())
                .add(CastingItems.NUGGET_MOLD.asItem())
                .add(CastingItems.PLATE_MOLD.asItem())
                .add(CastingItems.GEM_MOLD.asItem())
                .add(CastingItems.ROD_MOLD.asItem())
                .add(CastingItems.BALL_MOLD.asItem())
                .add(CastingItems.WIRE_MOLD.asItem())
        ;

        tag(CastingTags.Items.INGOT_MOLD).add(CastingItems.INGOT_MOLD.asItem());
        tag(CastingTags.Items.NUGGET_MOLD).add(CastingItems.NUGGET_MOLD.asItem());
        tag(CastingTags.Items.GEM_MOLD).add(CastingItems.GEM_MOLD.asItem());
        tag(CastingTags.Items.DUST_MOLD).add(CastingItems.DUST_MOLD.asItem());
        tag(CastingTags.Items.PLATE_MOLD).add(CastingItems.PLATE_MOLD.asItem());
        tag(CastingTags.Items.GEAR_MOLD).add(CastingItems.GEAR_MOLD.asItem());
        tag(CastingTags.Items.ROD_MOLD).add(CastingItems.ROD_MOLD.asItem());
        tag(CastingTags.Items.BLOCK_MOLD).add(CastingItems.BLOCK_MOLD.asItem());
        tag(CastingTags.Items.BALL_MOLD).add(CastingItems.BALL_MOLD.asItem());
        tag(CastingTags.Items.WIRE_MOLD).add(CastingItems.WIRE_MOLD.asItem());

        //Buckets
        for (var entry : FLUIDS_MAP.entrySet()) {
            tag(Tags.Items.BUCKETS).add(entry.getValue().getBucket());
        }

        //Processing Tags
        tag(TagKey.create(Registries.ITEM, Identifier.parse("c:storage_blocks/glowstone"))).add(Blocks.GLOWSTONE.asItem());
        tag(TagKey.create(Registries.ITEM, Identifier.parse("c:storage_blocks/quartz"))).add(Blocks.QUARTZ_BLOCK.asItem());

    }
}
