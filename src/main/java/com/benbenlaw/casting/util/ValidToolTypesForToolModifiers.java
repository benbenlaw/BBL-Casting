package com.benbenlaw.casting.util;
/*
import com.benbenlaw.casting.config.ModifierSetsConfig;
import com.benbenlaw.casting.item.EquipmentModifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.benbenlaw.casting.item.EquipmentModifier.*;

public class ValidToolTypesForToolModifiers {

    //Tool Types
    public static final String ALL_MODIFIERS = "all_modifiers";
    public static final String PICKAXE_MODIFIERS = "pickaxe_modifiers";
    public static final String AXE_MODIFIERS = "axe_modifiers";
    public static final String SHOVEL_MODIFIERS = "shovel_modifiers";
    public static final String HOE_MODIFIERS = "hoe_modifiers";
    public static final String SWORD_MODIFIERS = "sword_modifiers";
    public static final String PAXEL_MODIFIERS = "paxel_modifiers";
    public static final String SHEAR_MODIFIERS = "shear_modifiers";
    public static final String HELMET_MODIFIERS = "helmet_modifiers";
    public static final String CHESTPLATE_MODIFIERS = "chestplate_modifiers";
    public static final String LEGGINGS_MODIFIERS = "leggings_modifiers";
    public static final String BOOTS_MODIFIERS = "boots_modifiers";
    public static final String BODY_MODIFIERS = "body_modifiers";

    //Map
    public static final Map<String, List<EquipmentModifier>> DEFAULT_MODIFIERS;
    public static Map<String, List<EquipmentModifier>> VALID_MODIFIERS;

    static {

        //Add all modifiers to this

        DEFAULT_MODIFIERS = Map.ofEntries(Map.entry(ALL_MODIFIERS,
                        List.of(JETS, SOULBOUND, FEATHER_FALLING, FLIGHT, SPEED, LAVA_WALKER, WATER_WALKER, NIGHT_VISION, WATER_BREATHING, STEP_ASSIST, MAGNET,
                            PROTECTION, TELEPORTING, EXCAVATION, IGNITE, EFFICIENCY, UNBREAKING, FORTUNE, REPAIRING, SILK_TOUCH, TORCH_PLACING,
                            AUTO_SMELT, LOOTING, SHARPNESS, BEHEADING, LIFESTEAL, KNOCKBACK)),

            //Tools
            Map.entry(PICKAXE_MODIFIERS, List.of(EQUIPMENT_LEVEL, TELEPORTING, EXCAVATION, EFFICIENCY, UNBREAKING, FORTUNE, REPAIRING,
                            SILK_TOUCH, TORCH_PLACING, AUTO_SMELT, SOULBOUND)),

            Map.entry(AXE_MODIFIERS, List.of(EQUIPMENT_LEVEL, TELEPORTING, EXCAVATION, EFFICIENCY, UNBREAKING, FORTUNE,
                    REPAIRING, SILK_TOUCH, LOOTING, SOULBOUND)),

            Map.entry(SHOVEL_MODIFIERS, List.of(EQUIPMENT_LEVEL, TELEPORTING, EXCAVATION, EFFICIENCY, UNBREAKING,
                    FORTUNE, REPAIRING, SILK_TOUCH, SOULBOUND)),

            Map.entry(PAXEL_MODIFIERS, List.of(EQUIPMENT_LEVEL, TELEPORTING, EXCAVATION, EFFICIENCY, UNBREAKING,
                    FORTUNE, REPAIRING, SILK_TOUCH, TORCH_PLACING, AUTO_SMELT, SOULBOUND)),

            Map.entry(SHEAR_MODIFIERS, List.of(EQUIPMENT_LEVEL, EXCAVATION, UNBREAKING, REPAIRING, SOULBOUND)),

            Map.entry(HOE_MODIFIERS, List.of(EQUIPMENT_LEVEL, EXCAVATION, EFFICIENCY, UNBREAKING, FORTUNE, REPAIRING, SILK_TOUCH, SOULBOUND)),

            Map.entry(SWORD_MODIFIERS, List.of(EQUIPMENT_LEVEL, TELEPORTING, UNBREAKING, LOOTING, SHARPNESS, REPAIRING,
                    BEHEADING, KNOCKBACK, LIFESTEAL, IGNITE, SOULBOUND)),

            //Armor
            Map.entry(HELMET_MODIFIERS, List.of(EQUIPMENT_LEVEL, NIGHT_VISION, WATER_BREATHING, MAGNET, REPAIRING, UNBREAKING, PROTECTION, SOULBOUND)),
            Map.entry(CHESTPLATE_MODIFIERS, List.of(EQUIPMENT_LEVEL, FLIGHT, MAGNET, REPAIRING, UNBREAKING, PROTECTION, SOULBOUND, JETS)),
            Map.entry(LEGGINGS_MODIFIERS, List.of(EQUIPMENT_LEVEL, SPEED, MAGNET, REPAIRING, UNBREAKING, PROTECTION, SOULBOUND, JETS)),
            Map.entry(BOOTS_MODIFIERS, List.of(EQUIPMENT_LEVEL, FEATHER_FALLING, SPEED, LAVA_WALKER, WATER_WALKER,
                    STEP_ASSIST, MAGNET, REPAIRING, UNBREAKING, PROTECTION, SOULBOUND, JETS)),

            //Is body animals I think it is so will leave this empty
            Map.entry(BODY_MODIFIERS, List.of()));

            Map<String, List<EquipmentModifier>> customGroups = ModifierSetsConfig.getCustomModifierGroups();

            // Merge custom groups on top of defaults
            Map<String, List<EquipmentModifier>> merged = new HashMap<>(DEFAULT_MODIFIERS);

            if (ModifierSetsConfig.DISABLE_ALL_DEFAULT_MODIFIER_SETS.get()) {
                merged.clear();
            }

            merged.putAll(customGroups);

            // All modifiers including custom
            VALID_MODIFIERS = Collections.unmodifiableMap(merged);
    }


}

 */
