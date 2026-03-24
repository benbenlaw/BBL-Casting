package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class CastingLangProvider extends LanguageProvider {

    public CastingLangProvider(PackOutput output) {
        super(output, Casting.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.casting", "Casting");

        //Tooltip
        add("tooltip.casting.fluids_header", "Fluids:");

        //JEI
        add("jei.casting.solidifier", "Solidifier");
        add("jei.casting.mixing", "Mixing");
        add("jei.casting.melting", "Melting");

        //Keys
        addKeyTranslation("category", "Casting");
        addKeyTranslation("helmet_hotkey", "Toggle Helmet Modifiers");
        addKeyTranslation("chestplate_hotkey", "Toggle Chestplate Modifiers");
        addKeyTranslation("leggings_hotkey", "Toggle Leggings Modifiers");
        addKeyTranslation("boots_hotkey", "Toggle Boots Modifiers");

        //Misc Items
        addItemTranslation("black_brick", "Black Brick");
        addItemTranslation("fluid_mover", "Fluid Mover");

        //Blocks
        addBlockTranslation("multiblock_controller", "Multiblock Controller");
        addBlockTranslation("multiblock_fuel_tank", "Multiblock Fuel Tank");
        addBlockTranslation("multiblock_coolant_tank", "Multiblock Coolant Tank");
        addBlockTranslation("multiblock_solidifier", "Multiblock Solidifier");
        addBlockTranslation("black_bricks", "Black Bricks");
        addBlockTranslation("black_brick_glass", "Black Brick Glass");
        addBlockTranslation("multiblock_valve", "Multiblock Valve");
        addBlockTranslation("multiblock_mixer", "Multiblock Mixer ");
        addBlockTranslation("multiblock_regulator", "Multiblock Regulator");

        //OG Casting
        addBlockTranslation("controller", "Simple Controller");
        addBlockTranslation("solidifier", "Simple Solidifier");
        addBlockTranslation("mixer", "Simple Mixer");
        addBlockTranslation("equipment_modifier", "Simple Equipment Modifier");
        addBlockTranslation("tank", "Simple Tank");
        addBlockTranslation("mixer_whisk", "Simple Mixer Whisk");

        //Multiblock - Controller
        addChatTranslation("multiblock_controller.no_valid_block", "No valid blocks found, must be exactly 2");
        addChatTranslation("multiblock_controller.only_one_valid_block", "Only one valid block found, must be exactly 2");
        addChatTranslation("multiblock_controller.too_many_valid_blocks", "Too many valid blocks found, must be exactly 2");
        addChatTranslation("multiblock_controller.item_spill", "Items will spill out the controller in 3 seconds unless multiblock is formed!");
        addChatTranslation("multiblock_controller.items_dropped", "Items dropped from the controller, multiblock not formed!");

        //Toggle Armor Modifiers
        addChatTranslation("information.helmet_toggle", "- Toggling Helmet Modifiers");
        addChatTranslation("information.chestplate_toggle", "- Toggling Chestplate Modifiers");
        addChatTranslation("information.leggings_toggle", "- Toggling Leggings Modifiers");
        addChatTranslation("information.boots_toggle", "- Toggling Boots Modifiers");

        //Gui addGUITranslation
        addGUITranslation("multiblock_controller.off", "Turn on the Multiblock at the top left of the GUI! If you can't see the button decrease your GUI scale!");
        addGUITranslation("multiblock_controller.no_fuel_tank", "No Fuel Tank Found!");
        addGUITranslation("multiblock_controller.no_controller", "No Controller Found!");

        addGUITranslation("buttons.no_off", "On/Off Button");
        addGUITranslation("buttons.fluid", "Filter: Empty, Click to cycle fluids");
        addGUITranslation("buttons.remove_fluid", "Filter: %s, Shift Click to remove filter");
        addGUITranslation("buttons.no_alloy", "Filter: Empty, Click to cycle");
        addGUITranslation("buttons.cycle_valid_alloy", "Filter: %s, Click to cycle, Found %s Alloys");
        addGUITranslation("buttons.not_hot_enough", "Current fuel not hot enough for at least one item");
        addGUITranslation("buttons.no_fuel", "No fuel found in fuel tank");
        addGUITranslation("buttons.not_enough_space", "Not enough space in main tank");
        addGUITranslation("buttons.multiblock_not_found", "Multiblock not formed, are there gaps in the structure?");
        addGUITranslation("buttons.waiting_for_recipe", "Ready to work!");
        addGUITranslation("buttons.filtering_disabled", "Filter: Disabled, Click to enable");
        addGUITranslation("buttons.filtering_enabled", "Filter: Enabled, Click to disable");
        addGUITranslation("buttons.dump_fluid", "Click to Dump Fluid!");
        addGUITranslation("buttons.shift_dump_fluid", "Hold Shift to Dump Fluid!");
        addGUITranslation("buttons.not_high_enough_level", "Equipment not high enough level to add this modifier!");
        addGUITranslation("buttons.at_max_level", "Equipment at Max Level for this modifier!");
        addGUITranslation("buttons.wrong_fluid", "Wrong Fluid in Tank!");
        addGUITranslation("buttons.not_enough_fluid", "Not Enough Fluid in Tank!");
        addGUITranslation("buttons.no_regulators", "No Regulators detected, add for reserving space!");
        addGUITranslation("buttons.regulator_count", "Tank Partitioned: %smb for %s fluids");
        addGUITranslation("buttons.output_full_mixer", "The output tank is full, unable to craft mixer result!");
        addGUITranslation("buttons.remove_fluid_right_click", "Filter: %s, Right Click to remove");

        //Jei Translations
        addGUITranslation("jei.melting", "Melting");
        addGUITranslation("jei.melting_temp", "%s Min Temp");
        addGUITranslation("jei.mixing", "Mixing");
        addGUITranslation("jei.solidifier", "Solidifier");
        addGUITranslation("jei.fuel", "Fuels");
        addGUITranslation("jei.fuel_temp", "%s Fuel Temp");
        addGUITranslation("jei.fuel_speed", "%s ticks per recipe");
        addGUITranslation("jei.coolant", "Coolants");
        addGUITranslation("jei.coolant_speed", "%s ticks per recipe");
        addGUITranslation("jei.multiblock_controller_output", "%smB in Multiblock Controller");
        addGUITranslation("jei.simple_controller_output", "%smB in Simple Controller");

        addGUITranslation("jei.information.solidifier",   "The Solidifier is used to turn your molten resource into useful item! \n\nThis can use an optional Fuel Tank adjacent to it, cooler fluids (below 1000) in the tank will make the Solidifier faster\n\nFluid can be locked to prevent another fluids from going in, click the lock button next to the fluid tank.\n\nHolding Shift whilst above the tank and left clicking will remove the fluid from the tank!\n\nStores fluids when broken!");
        addGUITranslation("jei.information.mixer", "The Mixer can be used to mix molten fluids together! \n\nHolding Shift whilst above a certain tank and left clicking will remove the fluid from the that tank!\n\nStores fluids when broken!");
        addGUITranslation("jei.information.controller", "The Controller can be used to melt items into their molten variants! \n\nThis needs a Fuel Tank to work. The Fuel Tank must contain a Fuel that is hot enough to melt the item, Hotter fluids melt faster\n\nHolding Shift whilst above a certain tank and left clicking will remove the fluid from the that tank!\n\nStores fluids when broken!");
        addGUITranslation("jei.information.tank", "The Tank stores the fuel used in the Controller to melt items!\n\nCan also be used with the Solidifier colder fluids in the tank makes the Solidifier faster \n\nStores fluids when broken!");
        addGUITranslation("jei.information.equipment_modifier", "Used to apply modifiers to your equipment!, Your first modifier will add Level 1 to your equipment additional modifiers require an additional level. Use your tools to Level Up and get more modifier slots.\n\nUsing a Repairing Mold will allow you to repair Equipment using its molten variants! \n\nSearch Equipment Modifiers in JEI to view the modifiers!\n\nPlace the right items and or fluids in the Equipment Modifier then add a valid piece of Equipment to add that effect!\n\nHolding Shift whilst above the tank and left clicking will remove the fluid from the tank!\n\nStores fluids when broken!");
        addGUITranslation("jei.information.fluid_mover", "Used to move fluids between tanks, Holds up to 8000mb of a single fluid. \n\nClick on a tank to collect fluid and click on another tank to add!\n\nWill try to fill the selected tank before collecting fluids!");
        addGUITranslation("jei.information.mixer_whisk", "Used to speed up the mixer when placed above it. \n\nFor each whisk above the mixer the tick rate is decreased by 40 ticks!\n\nAdditional whisks placed after the tick rate is at its lowest (20) will have no effect!");

        //Molds
        addItemTranslation("block_mold", "Block Mold");
        addItemTranslation("gear_mold", "Gear Mold");
        addItemTranslation("ingot_mold", "Ingot Mold");
        addItemTranslation("nugget_mold", "Nugget Mold");
        addItemTranslation("plate_mold", "Plate Mold");
        addItemTranslation("rod_mold", "Rod Mold");
        addItemTranslation("gem_mold", "Gem Mold");
        addItemTranslation("dust_mold", "Dust Mold");
        addItemTranslation("ball_mold", "Ball Mold");
        addItemTranslation("wire_mold", "Wire Mold");
        addItemTranslation("repairing_mold", "Repairing Mold");

        //Equipment Modifiers
        addItemTranslation("auto_smelt", "Equipment Modifier: Auto Smelt");
        addItemTranslation("beheading", "Equipment Modifier: Beheading");
        addItemTranslation("efficiency", "Equipment Modifier: Efficiency");
        addItemTranslation("excavation", "Equipment Modifier: Excavation");
        addItemTranslation("fortune", "Equipment Modifier: Fortune");
        addItemTranslation("ignite", "Equipment Modifier: Ignite");
        addItemTranslation("knockback", "Equipment Modifier: Knockback");
        addItemTranslation("lifesteal", "Equipment Modifier: Lifesteal");
        addItemTranslation("looting", "Equipment Modifier: Looting");
        addItemTranslation("magnet", "Equipment Modifier: Magnet");
        addItemTranslation("protection", "Equipment Modifier: Protection");
        addItemTranslation("repairing", "Equipment Modifier: Repairing");
        addItemTranslation("sharpness", "Equipment Modifier: Sharpness");
        addItemTranslation("silk_touch", "Equipment Modifier: Silk Touch");
        addItemTranslation("step_assist", "Equipment Modifier: Step Assist");
        addItemTranslation("teleporting", "Equipment Modifier: Teleporting");
        addItemTranslation("torch_placing", "Equipment Modifier: Torch Placing");
        addItemTranslation("unbreaking", "Equipment Modifier: Unbreaking");
        addItemTranslation("water_walker", "Equipment Modifier: Water Walker");
        addItemTranslation("lava_walker", "Equipment Modifier: Lava Walker");
        addItemTranslation("speed", "Equipment Modifier: Speed");
        addItemTranslation("water_breathing", "Equipment Modifier: Water Breathing");
        addItemTranslation("night_vision", "Equipment Modifier: Night Vision");
        addItemTranslation("flight", "Equipment Modifier: Flight");
        addItemTranslation("feather_falling", "Equipment Modifier: Feather Falling");
        addItemTranslation("soulbound", "Equipment Modifier: Soulbound");
        addItemTranslation("jets", "Equipment Modifier: Jets");

        addItemTranslation("equipment_level", "Equipment Modifier: Level Up");

        //Tooltips
        addTooltipTranslation("information.repairing_mold", "When used in the Equipment Modifier, this will allow you to repair your equipment using its molten variant!");
        addTooltipTranslation("information.valid_tool_types", "Valid Equipment;");
        addTooltipTranslation("information.shift_to_disable", "- Hold SHIFT to disable this modifier");
        addTooltipTranslation("information.shift_to_toggle", "- Hold SHIFT and Right Click to disable or enable this modifier");
        addTooltipTranslation("information.keybind_to_toggle", "- Press chosen keybind (see controls) to disable or enable this modifier");

        addTooltipTranslation("information.modifiers", "Modifiers - %s/%s");
        //addTooltipTranslation("information.equipment_level", "Equipment Level %s XP: %s/%s");
        addTooltipTranslation("information.equipment_level", "Level up a piece of Equipment to add a new modifier slot up to level %s");
        addTooltipTranslation("information.equipment_level_item", "Gives your equipment a level, each level adds an additional modifier slot, up to %s levels");
        addTooltipTranslation("information.auto_smelt", "Allows the tool to automatically smelt items it mines up to level %s");
        addTooltipTranslation("information.beheading", "Gives a tool Beheading, drops mob heads");
        addTooltipTranslation("information.efficiency", "Gives a tool Efficiency up to level %s");
        addTooltipTranslation("information.excavation", "Gives a tool Excavation, allows the tool to mine a bigger area up to level %s");
        addTooltipTranslation("information.fortune", "Gives a tool Fortune up to level %s");
        addTooltipTranslation("information.ignite", "Gives a tool Ignite, ignites enemies on hit up to level %s");
        addTooltipTranslation("information.knockback", "Gives a tool Knockback up to level %s");
        addTooltipTranslation("information.lifesteal", "Gives a tool Lifesteal, restores health when damaging enemies up to level %s");
        addTooltipTranslation("information.looting", "Gives a tool Looting up to level %s");
        addTooltipTranslation("information.magnet", "Gives armor Magnet, attracts items to the player up to level %s, block drops also enter the player's inventory");
        addTooltipTranslation("information.protection", "Gives armor Protection up to level %s");
        addTooltipTranslation("information.repairing", "Gives a tool Repairing up to level %s");
        addTooltipTranslation("information.sharpness", "Gives a tool Sharpness up to level %s");
        addTooltipTranslation("information.silk_touch", "Gives a tool Silk Touch up to level %s");
        addTooltipTranslation("information.step_assist", "Gives armor Step Assist, allows the player to step up blocks up to level %s");
        addTooltipTranslation("information.teleporting", "Gives a tool Teleporting, allows the tool to teleport you to far away blocks up to level %s. Costs 5 durability per teleport");
        addTooltipTranslation("information.torch_placing", "Allows the tool to place torches on right click up to level %s");
        addTooltipTranslation("information.unbreaking", "Gives a tool Unbreaking up to level %s");
        addTooltipTranslation("information.water_walker", "Gives armor Water Walker, allows the player to walk on water up to level %s");
        addTooltipTranslation("information.lava_walker", "Gives armor Lava Walker, allows the player to walk on lava up to level %s");
        addTooltipTranslation("information.speed", "Gives armor Speed, allows the player to move faster up to level %s");
        addTooltipTranslation("information.water_breathing", "Gives armor Water Breathing, allows the player to breathe underwater");
        addTooltipTranslation("information.night_vision", "Gives armor Night Vision, allows the player to see in the dark");
        addTooltipTranslation("information.flight", "Gives armor Creative Flight");
        addTooltipTranslation("information.feather_falling", "Gives armor Feather Falling, each level adds 10%% damage reduction from falling up to level %s");
        addTooltipTranslation("information.soulbound", "Returns the item to the player after death");
        addTooltipTranslation("information.jets", "Gives armor Jets, allows the player to jet up in the air, up to level %s");

        addTooltipTranslation("stats.modifiers", "Modifiers - %s/%s");
        addTooltipTranslation("stats.equipment_level", "Equipment Level %s XP: %s/%s");
        addTooltipTranslation("stats.repairing_mold", "When inside the Equipment Modifier, this will allow you to repair your equipment using its molten variant!");
        addTooltipTranslation("stats.auto_smelt", "- Auto Smelt");
        addTooltipTranslation("stats.beheading", "- Beheading");
        addTooltipTranslation("stats.efficiency", "- Efficiency - %s");
        addTooltipTranslation("stats.excavation", "- Excavation - %s (%sx%s Area)");
        addTooltipTranslation("stats.fortune", "- Fortune - %s");
        addTooltipTranslation("stats.ignite", "- Ignite - Burn for %s Second(s)");
        addTooltipTranslation("stats.knockback", "- Knockback - %s");
        addTooltipTranslation("stats.lifesteal", "- Lifesteal - %s%% Damage Restored");
        addTooltipTranslation("stats.looting", "- Looting - %s");
        addTooltipTranslation("stats.magnet", "- Magnet - %s");
        addTooltipTranslation("stats.protection", "- Protection - %s");
        addTooltipTranslation("stats.repairing", "- Repairing - %s");
        addTooltipTranslation("stats.sharpness", "- Sharpness - %s (+%s Attack Damage)");
        addTooltipTranslation("stats.silk_touch", "- Silk Touch");
        addTooltipTranslation("stats.step_assist", "- Step Assist - %s");
        addTooltipTranslation("stats.teleporting", "- Teleporting - %s (%s Blocks)");
        addTooltipTranslation("stats.torch_placing", "- Torch Placing");
        addTooltipTranslation("stats.unbreaking", "- Unbreaking - %s");
        addTooltipTranslation("stats.water_walker", "- Water Walker");
        addTooltipTranslation("stats.lava_walker", "- Lava Walker");
        addTooltipTranslation("stats.speed", "- Speed - %s");
        addTooltipTranslation("stats.water_breathing", "- Water Breathing");
        addTooltipTranslation("stats.night_vision", "- Night Vision");
        addTooltipTranslation("stats.flight", "- Flight");
        addTooltipTranslation("stats.feather_falling", "- Feather Falling - %s");
        addTooltipTranslation("stats.soulbound", "- Soulbound");
        addTooltipTranslation("stats.jets", "- Jets - %s");

        addTooltipTranslation("all_modifiers", "- All");
        addTooltipTranslation("pickaxe_modifiers", "- Pickaxes");
        addTooltipTranslation("shovel_modifiers", "- Shovels");
        addTooltipTranslation("axe_modifiers", "- Axes");
        addTooltipTranslation("hoe_modifiers", "- Hoes");
        addTooltipTranslation("sword_modifiers", "- Swords");
        addTooltipTranslation("shear_modifiers", "- Shears");
        addTooltipTranslation("paxel_modifiers", "- Paxels");
        addTooltipTranslation("chestplate_modifiers", "- Chestplates");
        addTooltipTranslation("helmet_modifiers", "- Helmets");
        addTooltipTranslation("leggings_modifiers", "- Leggings");
        addTooltipTranslation("boots_modifiers", "- Boots");





        //Fluid Files
        for (var entry : FLUIDS_MAP.entrySet()) {
            String fluidName = entry.getKey();
            String fluidDisplayName = capitalizeFirstLetterOfEachWord(fluidName);

            addBlockTranslation(fluidName, fluidDisplayName);
            addItemTranslation(fluidName + "_bucket", fluidDisplayName + " Bucket");
        }



    }

    private void addItemTranslation(String name, String translation) {
        add("item." + Casting.MOD_ID + "." + name, translation);
    }
    private void addBlockTranslation(String name, String translation) {
        add("block." + Casting.MOD_ID + "." + name, translation);
    }

    private void addChatTranslation(String name, String translation) {
        add("chat." + Casting.MOD_ID + "." + name, translation);
    }
    private void addGUITranslation(String name, String translation) {
        add("gui." + Casting.MOD_ID + "." + name, translation);
    }
    private void addTooltipTranslation(String name, String translation) {
        add("tooltips." + Casting.MOD_ID + "." + name, translation);
    }
    private void addKeyTranslation(String name, String translation) {
        add("key." + Casting.MOD_ID + "." + name, translation);
    }


    public static String capitalizeFirstLetterOfEachWord(String input) {
        String[] words = input.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase()) // Capitalize first letter
                        .append(word.substring(1).toLowerCase()) // Keep the rest lowercase
                        .append(" ");
            }
        }
        return result.toString().trim(); // Remove trailing space
    }

}
