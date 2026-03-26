package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.util.CastingTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CastingBlockTags extends BlockTagsProvider {

    CastingBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Casting.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CastingBlocks.BLACK_BRICKS.get())
                .add(CastingBlocks.BLACK_BRICK_GLASS.get())
                .add(CastingBlocks.SOLIDIFIER.get())
                .add(CastingBlocks.MIXER.get())
                .add(CastingBlocks.TANK.get())
                .add(CastingBlocks.CONTROLLER.get())
        ;

        //Processing Tags
        tag(TagKey.create(Registries.BLOCK, Identifier.parse("c:storage_blocks/glowstone"))).add(Blocks.GLOWSTONE);
        tag(TagKey.create(Registries.BLOCK, Identifier.parse("c:storage_blocks/quartz"))).add(Blocks.QUARTZ_BLOCK);
    }

}
