package com.benbenlaw.casting.block;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.custom.ControllerBlock;
import com.benbenlaw.casting.block.custom.MixerBlock;
import com.benbenlaw.casting.block.custom.SolidifierBlock;
import com.benbenlaw.casting.block.custom.TankBlock;
import com.benbenlaw.casting.item.CastingItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class CastingBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Casting.MOD_ID);

    public static final DeferredBlock<Block> BLACK_BRICKS = registerBlock("black_bricks",
            properties -> new Block(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> BLACK_BRICK_GLASS = registerBlock("black_brick_glass",
            properties -> new TransparentBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.GLASS)));

    public static final DeferredBlock<Block> CONTROLLER = registerBlock("controller",
            properties -> new ControllerBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> SOLIDIFIER = registerBlock("solidifier",
            properties -> new SolidifierBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> MIXER = registerBlock("mixer",
            properties -> new MixerBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> TANK = registerBlock("tank",
            properties -> new TankBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        CastingItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
}
