package com.benbenlaw.casting.fluid;

import com.benbenlaw.core.tag.ResourceType;

import java.util.ArrayList;
import java.util.List;

public record FluidData(String name, String stillTexture, String flowTexture, int tint, FluidProcessingData fluidProduceType) {

    public static List<FluidData> FLUID_DEFINITIONS;

    static {
        List<FluidData> fluidList = new ArrayList<>();

        // --- Refined Storage ---
        fluidList.add(new FluidData("molten_quartz_enriched_iron", "block/molten_still", "block/molten_flow", 0xFF9d9c9a,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_quartz_enriched_copper", "block/molten_still", "block/molten_flow", 0xFFEB8669,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));

        // --- Actually Additions ---
        fluidList.add(new FluidData("molten_black_quartz", "block/molten_still", "block/molten_flow", 0xFF586e75,
                new FluidProcessingData(1000, 250, ProcessingType.GEMS4)));

        // --- AE2 ---
        fluidList.add(new FluidData("molten_certus_quartz", "block/molten_still", "block/molten_flow", 0xFFaae6fc,
                new FluidProcessingData(1000, 250, ProcessingType.GEMS4)));
        fluidList.add(new FluidData("molten_charged_certus_quartz", "block/molten_still", "block/molten_flow", 0xFFd7fcfc,
                new FluidProcessingData(1000, 250, ProcessingType.GEMS4)));
        fluidList.add(new FluidData("molten_fluix", "block/molten_still", "block/molten_flow", 0xFF262a53,
                new FluidProcessingData(1000, 250, ProcessingType.GEMS4)));

        // --- Vanilla ---
        fluidList.add(new FluidData("molten_iron", "block/molten_still", "block/molten_flow", 0xFFd5d5d5,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_gold", "block/molten_still", "block/molten_flow", 0xFFfaf25e,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_copper", "block/molten_still", "block/molten_flow", 0xFFbf5935,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_netherite", "block/molten_still", "block/molten_flow", 0xFF4c484c,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_debris", "block/molten_still", "block/molten_flow", 0xFF7d5f58,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));

        fluidList.add(new FluidData("molten_diamond", "block/molten_still", "block/molten_flow", 0xFF9ff8e5,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));
        fluidList.add(new FluidData("molten_emerald", "block/molten_still", "block/molten_flow", 0xFF40f082,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));
        fluidList.add(new FluidData("molten_lapis", "block/molten_still", "block/molten_flow", 0xFF1c52a6,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));
        fluidList.add(new FluidData("molten_quartz", "block/molten_still", "block/molten_flow", 0xFFdad1c4,
                new FluidProcessingData(1000, 250, ProcessingType.GEMS4)));
        fluidList.add(new FluidData("molten_coal", "block/molten_still", "block/molten_flow", 0xFF252525,
                new FluidProcessingData(1000, 80, ProcessingType.GEMS9)));

        fluidList.add(new FluidData("molten_redstone", "block/molten_still", "block/molten_flow", 0xFFfc0000,
                new FluidProcessingData(1000, 100, ProcessingType.DUST9)));
        fluidList.add(new FluidData("molten_glowstone", "block/molten_still", "block/molten_flow", 0xFFfcba5d,
                new FluidProcessingData(1000, 250, ProcessingType.DUST4)));

        fluidList.add(new FluidData("molten_obsidian", "block/molten_still", "block/molten_flow", 0xFF3a2753,
                new FluidProcessingData(1200, 1000, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_glass", "block/thin_still", "block/thin_flow", 0xFF55575d,
                new FluidProcessingData(1000, 1000, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_stone", "block/molten_still", "block/molten_flow", 0xFF737373,
                new FluidProcessingData(1000, 1000, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_end_stone", "block/molten_still", "block/molten_flow", 0xFFdbe3a2,
                new FluidProcessingData(1000, 1000, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_soul", "block/molten_still", "block/molten_flow", 0xFF413027,
                new FluidProcessingData(1000, 1000, ProcessingType.CUSTOM)));

        fluidList.add(new FluidData("molten_ender", "block/molten_still", "block/molten_flow", 0xFF083c3c,
                new FluidProcessingData(1000, 250, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_blaze", "block/molten_still", "block/molten_flow", 0xFFf5a100,
                new FluidProcessingData(1200, 250, ProcessingType.CUSTOM)));

        // --- Coolants ---
        fluidList.add(new FluidData("chilled_water", "block/thin_still", "block/thin_flow", 0xFF345baa,
                new FluidProcessingData(400, 0, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("iced_water", "block/thin_still", "block/thin_flow", 0xFF5e77a3,
                new FluidProcessingData(200, 0, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("super_coolant", "block/thin_still", "block/thin_flow", 0xFF70c1cf,
                new FluidProcessingData(0, 0, ProcessingType.CUSTOM)));

        // --- Casting Mod Specific ---
        fluidList.add(new FluidData("molten_black_brick", "block/molten_still", "block/molten_flow", 0xFF646565,
                new FluidProcessingData(1000, 250, ProcessingType.CUSTOM)));
        fluidList.add(new FluidData("molten_experience", "block/molten_still", "block/molten_flow", 0xFF7cee4b,
                new FluidProcessingData(1000, 0, ProcessingType.CUSTOM)));

        // --- Common Modded Metals/Gems ---
        fluidList.add(new FluidData("molten_ruby", "block/molten_still", "block/molten_flow", 0xFFf57fa8,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));
        fluidList.add(new FluidData("molten_sapphire", "block/molten_still", "block/molten_flow", 0xFF439ef9,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));
        fluidList.add(new FluidData("molten_peridot", "block/molten_still", "block/molten_flow", 0xFFe5ef43,
                new FluidProcessingData(1000, 90, ProcessingType.GEMS9)));

        fluidList.add(new FluidData("molten_tin", "block/molten_still", "block/molten_flow", 0xFFdff1f8,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_lead", "block/molten_still", "block/molten_flow", 0xFF8b9cd0,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_osmium", "block/molten_still", "block/molten_flow", 0xFFafc6cc,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_uranium", "block/molten_still", "block/molten_flow", 0xFFe5eac0,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_silver", "block/molten_still", "block/molten_flow", 0xFFd1dadf,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_nickel", "block/molten_still", "block/molten_flow", 0xFFc5beac,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_zinc", "block/molten_still", "block/molten_flow", 0xFFc7dddb,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_platinum", "block/molten_still", "block/molten_flow", 0xFFa8e4fc,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_aluminum", "block/molten_still", "block/molten_flow", 0xFFd9e1e3,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_iridium", "block/molten_still", "block/molten_flow", 0xFFb7b0ac,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_silicon", "block/molten_still", "block/molten_flow", 0xFF6c7273,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_titanium", "block/molten_still", "block/molten_flow", 0xFFEAFFF9,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));

        // --- Alloys ---
        fluidList.add(new FluidData("molten_bronze", "block/molten_still", "block/molten_flow", 0xFF97602b,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_electrum", "block/molten_still", "block/molten_flow", 0xFFf2ee7e,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_steel", "block/molten_still", "block/molten_flow", 0xFF898b8d,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_invar", "block/molten_still", "block/molten_flow", 0xFF939393,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_signalum", "block/molten_still", "block/molten_flow", 0xFFe06c0c,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_lumium", "block/molten_still", "block/molten_flow", 0xFFfcfcd6,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_enderium", "block/molten_still", "block/molten_flow", 0xFF073131,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_brass", "block/molten_still", "block/molten_flow", 0xFFecb63d,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_constantan", "block/molten_still", "block/molten_flow", 0xFFd4bc61,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_end_steel", "block/molten_still", "block/molten_flow", 0xFFcccf8f,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_soularium", "block/molten_still", "block/molten_flow", 0xFF6a5b45,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_dark_steel", "block/molten_still", "block/molten_flow", 0xFF646068,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_pulsating_alloy", "block/molten_still", "block/molten_flow", 0xFF8bf9af,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_conductive_alloy", "block/molten_still", "block/molten_flow", 0xFFeed4b9,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_vibrant_alloy", "block/molten_still", "block/molten_flow", 0xFFfcfadf,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));
        fluidList.add(new FluidData("molten_energetic_alloy", "block/molten_still", "block/molten_flow", 0xFFf5a84f,
                new FluidProcessingData(1000, 90, ProcessingType.INGOT9)));

        FLUID_DEFINITIONS = fluidList;
    }


    public static int getTempByName(String name) {
        return FLUID_DEFINITIONS.stream()
                .filter(data -> data.name().equals(name))
                .findFirst()
                .map(data -> data.fluidProduceType().temp()) // Updated path
                .orElse(1000);
    }
}


