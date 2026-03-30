package com.benbenlaw.casting.item;

import com.benbenlaw.casting.Casting;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CastingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Casting.MOD_ID);

    //Molds
    public static final DeferredItem<Item> INGOT_MOLD = ITEMS.registerSimpleItem("ingot_mold");
    public static final DeferredItem<Item> NUGGET_MOLD = ITEMS.registerSimpleItem("nugget_mold");
    public static final DeferredItem<Item> GEAR_MOLD = ITEMS.registerSimpleItem("gear_mold");
    public static final DeferredItem<Item> PLATE_MOLD = ITEMS.registerSimpleItem("plate_mold");
    public static final DeferredItem<Item> ROD_MOLD = ITEMS.registerSimpleItem("rod_mold");
    public static final DeferredItem<Item> BLOCK_MOLD = ITEMS.registerSimpleItem("block_mold");
    public static final DeferredItem<Item> GEM_MOLD = ITEMS.registerSimpleItem("gem_mold");
    public static final DeferredItem<Item> DUST_MOLD = ITEMS.registerSimpleItem("dust_mold");
    public static final DeferredItem<Item> BALL_MOLD = ITEMS.registerSimpleItem("ball_mold");
    public static final DeferredItem<Item> WIRE_MOLD = ITEMS.registerSimpleItem("wire_mold");

    public static final DeferredItem<Item> BLACK_BRICK = ITEMS.registerSimpleItem("black_brick");

    public static final DeferredItem<Item> EXPERIENCE_BALL = ITEMS.registerItem("experience_ball",
            ExperienceBallItem::new, properties -> properties.stacksTo(16));
}

