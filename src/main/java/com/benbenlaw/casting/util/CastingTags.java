package com.benbenlaw.casting.util;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.core.util.CoreTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CastingTags{
    public static class Blocks extends CoreTags.Blocks {

        //Blocks
        public static final TagKey<Block> CONTROLLER_WALLS = tag(Casting.MOD_ID, "controller_walls");
        public static final TagKey<Block> CONTROLLER_FLOORS = tag(Casting.MOD_ID, "controller_floors");
        public static final TagKey<Block> CONTROLLER_EXTRA_BLOCKS = tag(Casting.MOD_ID, "controller_extra_blocks");
        public static final TagKey<Block> CONTROLLER_TANKS = tag(Casting.MOD_ID, "controller_tanks");
        public static final TagKey<Block> CONTROLLER_ALL = tag(Casting.MOD_ID, "controller_all");

        public static final TagKey<Block> EFFECTED_BY_LAVA_WALKER = tag(Casting.MOD_ID, "effected_by_lava_walker");
        public static final TagKey<Block> EFFECTED_BY_WATER_WALKER = tag(Casting.MOD_ID, "effected_by_water_walker");
    }

    public static class Items extends CoreTags.Items {
        //Items
        public static final TagKey<Item> MOLDS = tag(Casting.MOD_ID, "molds");
        public static final TagKey<Item> CONTROLLER_WALLS = tag(Casting.MOD_ID, "controller_walls");
        public static final TagKey<Item> CONTROLLER_FLOORS = tag(Casting.MOD_ID, "controller_floors");
        public static final TagKey<Item> CONTROLLER_EXTRA_BLOCKS = tag(Casting.MOD_ID, "controller_extra_blocks");
        public static final TagKey<Item> CONTROLLER_TANKS = tag(Casting.MOD_ID, "controller_tanks");
        public static final TagKey<Item> INGOT_MOLD = commonTag("molds/ingot");
        public static final TagKey<Item> NUGGET_MOLD = commonTag("molds/nugget");
        public static final TagKey<Item> GEM_MOLD = commonTag("molds/gem");
        public static final TagKey<Item> DUST_MOLD = commonTag("molds/dust");
        public static final TagKey<Item> PLATE_MOLD = commonTag("molds/plate");
        public static final TagKey<Item> GEAR_MOLD = commonTag("molds/gear");
        public static final TagKey<Item> ROD_MOLD = commonTag("molds/rod");
        public static final TagKey<Item> BALL_MOLD = commonTag("molds/ball");
        public static final TagKey<Item> WIRE_MOLD = commonTag("molds/wire");
        public static final TagKey<Item> BLOCK_MOLD = commonTag("molds/block");
        public static final TagKey<Item> SHARD_MOLD = commonTag("molds/shards");
        public static final TagKey<Item> SHARDS = commonTag("shards");
        public static final TagKey<Item> BALL_ITEMS = commonTag("ball_items");
        public static final TagKey<Item> MELTING_OUTPUT_AMOUNT_EFFECTED = tag(Casting.MOD_ID, "melting_output_amount_effected");
        public static final TagKey<Item> MELTING_PRODUCES_EXPERIENCE = tag(Casting.MOD_ID, "melting_produces_experience");
        public static final TagKey<Item> CAN_BE_DISABLED_WITH_SHIFT = tag(Casting.MOD_ID, "can_be_disabled_with_shift");
        public static final TagKey<Item> CAN_BE_TOGGLED_WITH_SHIFT = tag(Casting.MOD_ID, "can_be_toggled_with_shift");
        public static final TagKey<Item> CAN_BE_TOGGLED_WITH_KEYBIND = tag(Casting.MOD_ID, "can_be_toggled_with_keybind");
        public static final TagKey<Item> EQUIPMENT_MODIFIERS = tag(Casting.MOD_ID, "equipment_modifiers");

    }


}
