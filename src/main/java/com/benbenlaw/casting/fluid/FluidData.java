package com.benbenlaw.casting.fluid;

import java.util.ArrayList;
import java.util.List;

public record FluidData(String name, String stillTexture, String flowTexture, int tint, int temp) {

    public static List<FluidData> FLUID_DEFINITIONS;


    static {

        List<FluidData> fluidList = new ArrayList<>();

        //Refined Storage
        fluidList.add(new FluidData("molten_quartz_enriched_iron", "block/molten_still", "block/molten_flow", 0xFF9d9c9a, 1000));
        fluidList.add(new FluidData("molten_quartz_enriched_copper", "block/molten_still", "block/molten_flow", 0xFFEB8669, 1000));

        //Actually Additions
        fluidList.add(new FluidData("molten_black_quartz", "block/molten_still", "block/molten_flow", 0xFF586e75, 1000));

        //AE2
        fluidList.add(new FluidData("molten_certus_quartz", "block/molten_still", "block/molten_flow", 0xFFaae6fc, 1000));
        fluidList.add(new FluidData("molten_charged_certus_quartz", "block/molten_still", "block/molten_flow", 0xFFd7fcfc, 1000));
        fluidList.add(new FluidData("molten_fluix", "block/molten_still", "block/molten_flow", 0xFF262a53, 1000));

        //Vanilla
        fluidList.add(new FluidData("molten_iron", "block/molten_still", "block/molten_flow", 0xFFd5d5d5, 1000));
        fluidList.add(new FluidData("molten_gold", "block/molten_still", "block/molten_flow", 0xFFfaf25e, 1000));
        fluidList.add(new FluidData("molten_coal", "block/molten_still", "block/molten_flow", 0xFF252525, 1000));
        fluidList.add(new FluidData("molten_copper", "block/molten_still", "block/molten_flow", 0xFFbf5935, 1000));
        fluidList.add(new FluidData("molten_lapis", "block/molten_still", "block/molten_flow", 0xFF1c52a6, 1000));
        fluidList.add(new FluidData("molten_redstone", "block/molten_still", "block/molten_flow", 0xFFfc0000, 1000));
        fluidList.add(new FluidData("molten_quartz", "block/molten_still", "block/molten_flow", 0xFFdad1c4, 1000));
        fluidList.add(new FluidData("molten_obsidian", "block/molten_still", "block/molten_flow", 0xFF3a2753, 1200));
        fluidList.add(new FluidData("molten_diamond", "block/molten_still", "block/molten_flow", 0xFF9ff8e5, 1000));
        fluidList.add(new FluidData("molten_emerald", "block/molten_still", "block/molten_flow", 0xFF40f082, 1000));
        fluidList.add(new FluidData("molten_glass", "block/thin_still", "block/thin_flow", 0xFF55575d, 1000));
        fluidList.add(new FluidData("molten_debris", "block/molten_still", "block/molten_flow", 0xFF7d5f58, 1000));
        fluidList.add(new FluidData("molten_ender", "block/molten_still", "block/molten_flow", 0xFF083c3c, 1000));
        fluidList.add(new FluidData("molten_glowstone", "block/molten_still", "block/molten_flow", 0xFFfcba5d, 1000));
        fluidList.add(new FluidData("molten_end_stone", "block/molten_still", "block/molten_flow", 0xFFdbe3a2, 1000));
        fluidList.add(new FluidData("molten_soul", "block/molten_still", "block/molten_flow", 0xFF413027, 1000));
        fluidList.add(new FluidData("molten_stone", "block/molten_still", "block/molten_flow", 0xFF737373, 1000));
        fluidList.add(new FluidData("molten_blaze", "block/molten_still", "block/molten_flow", 0xFFf5a100, 1200));
        fluidList.add(new FluidData("chilled_water", "block/thin_still", "block/thin_flow", 0xFF345baa, 200));
        fluidList.add(new FluidData("iced_water", "block/thin_still", "block/thin_flow", 0xFF5e77a3, 200));
        fluidList.add(new FluidData("super_coolant", "block/thin_still", "block/thin_flow", 0xFF70c1cf, 0));

        //Casting
        fluidList.add(new FluidData("molten_black_brick", "block/molten_still", "block/molten_flow", 0xFF646565, 1000));
        fluidList.add(new FluidData("molten_experience", "block/molten_still", "block/molten_flow", 0xFF7cee4b, 1000));

        //Common
        fluidList.add(new FluidData("molten_ruby", "block/molten_still", "block/molten_flow", 0xFFf57fa8, 1000));
        fluidList.add(new FluidData("molten_sapphire", "block/molten_still", "block/molten_flow", 0xFF439ef9, 1000));
        fluidList.add(new FluidData("molten_peridot", "block/molten_still", "block/molten_flow", 0xFFe5ef43, 1000));
        fluidList.add(new FluidData("molten_tin", "block/molten_still", "block/molten_flow", 0xFFdff1f8, 1000));
        fluidList.add(new FluidData("molten_lead", "block/molten_still", "block/molten_flow", 0xFF8b9cd0, 1000));
        fluidList.add(new FluidData("molten_osmium", "block/molten_still", "block/molten_flow", 0xFFafc6cc, 1000));
        fluidList.add(new FluidData("molten_uranium", "block/molten_still", "block/molten_flow", 0xFFe5eac0, 1000));
        fluidList.add(new FluidData("molten_silver", "block/molten_still", "block/molten_flow", 0xFFd1dadf, 1000));
        fluidList.add(new FluidData("molten_nickel", "block/molten_still", "block/molten_flow", 0xFFc5beac, 1000));
        fluidList.add(new FluidData("molten_zinc", "block/molten_still", "block/molten_flow", 0xFFc7dddb, 1000));
        fluidList.add(new FluidData("molten_platinum", "block/molten_still", "block/molten_flow", 0xFFa8e4fc, 1000));
        fluidList.add(new FluidData("molten_aluminum", "block/molten_still", "block/molten_flow", 0xFFd9e1e3, 1000));
        fluidList.add(new FluidData("molten_iridium", "block/molten_still", "block/molten_flow", 0xFFb7b0ac, 1000));
        fluidList.add(new FluidData("molten_silicon", "block/molten_still", "block/molten_flow", 0xFF6c7273, 1000));

        //Alloys
        fluidList.add(new FluidData("molten_netherite", "block/molten_still", "block/molten_flow", 0xFF4c484c, 1000));
        fluidList.add(new FluidData("molten_bronze", "block/molten_still", "block/molten_flow", 0xFF97602b, 1000));
        fluidList.add(new FluidData("molten_electrum", "block/molten_still", "block/molten_flow", 0xFFf2ee7e, 1000));
        fluidList.add(new FluidData("molten_steel", "block/molten_still", "block/molten_flow", 0xFF898b8d, 1000));
        fluidList.add(new FluidData("molten_invar", "block/molten_still", "block/molten_flow", 0xFF939393, 1000));
        fluidList.add(new FluidData("molten_signalum", "block/molten_still", "block/molten_flow", 0xFFe06c0c, 1000));
        fluidList.add(new FluidData("molten_lumium", "block/molten_still", "block/molten_flow", 0xFFfcfcd6, 1000));
        fluidList.add(new FluidData("molten_enderium", "block/molten_still", "block/molten_flow", 0xFF073131, 1000));
        fluidList.add(new FluidData("molten_brass", "block/molten_still", "block/molten_flow", 0xFFecb63d, 1000));
        fluidList.add(new FluidData("molten_constantan", "block/molten_still", "block/molten_flow", 0xFFd4bc61, 1000));
        fluidList.add(new FluidData("molten_end_steel", "block/molten_still", "block/molten_flow", 0xFFcccf8f, 1000));
        fluidList.add(new FluidData("molten_soularium", "block/molten_still", "block/molten_flow", 0xFF6a5b45, 1000));
        fluidList.add(new FluidData("molten_dark_steel", "block/molten_still", "block/molten_flow", 0xFF646068, 1000));
        fluidList.add(new FluidData("molten_pulsating_alloy", "block/molten_still", "block/molten_flow", 0xFF8bf9af, 1000));
        fluidList.add(new FluidData("molten_conductive_alloy", "block/molten_still", "block/molten_flow", 0xFFeed4b9, 1000));
        fluidList.add(new FluidData("molten_vibrant_alloy", "block/molten_still", "block/molten_flow", 0xFFfcfadf, 1000));
        fluidList.add(new FluidData("molten_energetic_alloy", "block/molten_still", "block/molten_flow", 0xFFf5a84f, 1000));
        fluidList.add(new FluidData("molten_redstone_alloy", "block/molten_still", "block/molten_flow", 0xFFfa976d, 1000));

        //Last
        FLUID_DEFINITIONS = fluidList;

    }



    public static int getTempByName(String name) {
        return FLUID_DEFINITIONS.stream()
                .filter(data -> data.name().equals(name))
                .findFirst()
                .map(FluidData::temp)
                .orElse(1000);
    }

}


