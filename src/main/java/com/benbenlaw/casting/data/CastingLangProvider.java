package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.item.ExperienceBallItem;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

public class CastingLangProvider extends LanguageProvider {

    public CastingLangProvider(PackOutput output) {
        super(output, Casting.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.casting", "Casting");

        //Block
        addBlockTranslation("solidifier", "Solidifier");
        addBlockTranslation("controller", "Controller");
        addBlockTranslation("mixer", "Mixer");
        addBlockTranslation("black_bricks", "Black Bricks");
        addBlockTranslation("black_brick_glass", "Black Brick Glass");
        addBlockTranslation("tank", "Tank");

        //Item
        addItemTranslation("ingot_mold", "Ingot Mold");
        addItemTranslation("nugget_mold", "Nugget Mold");
        addItemTranslation("gear_mold", "Gear Mold");
        addItemTranslation("plate_mold", "Plate Mold");
        addItemTranslation("rod_mold", "Rod Mold");
        addItemTranslation("block_mold", "Block Mold");
        addItemTranslation("gem_mold", "Gem Mold");
        addItemTranslation("dust_mold", "Dust Mold");
        addItemTranslation("ball_mold", "Ball Mold");
        addItemTranslation("wire_mold", "Wire Mold");
        addItemTranslation("shard_mold", "Shard Mold");

        addItemTranslation("black_brick", "Black Brick");
        addItemTranslation("experience_ball", "Experience Ball");

        //Tooltip
        add("tooltip.casting.fluids_header", "Fluids:");
        add("tooltip.casting.no_fuel", "No Tank adjacent. place and fill with fuel to start melting!");
        add("tooltip.casting.no_coolant", "No Tank adjacent. Place and fill with coolant to speed up the solidifier!");
        add("tooltip.casting.experience_ball", "Right click to consume and gain experience");

        add("jei.casting.information.solidifier", "The Solidifier is a used to transform fluids into items often using molds. \n\n"
        + "A tank with cold fluids can be used to make the Solidifier solidify faster. \n\n"
                + "Placing next to a Controller will auto pull fluids from it.");

        add("jei.casting.information.controller", "The Controller is the heart of Casting. Used to melt items into the molten variants. \n\n"
        + "A tank with hot fluids adjacent to the Controller is required for the Controller to work. \n\n"
                + "Placing next to a Solidifier or Mixer will auto push fluids from the Controller to them.");

        add("jei.casting.information.mixer", "The Mixer is used to mix fluids together. \n\n"
                + "Controllers will push fluids into this, Mixer will push fluids into Solidifiers.");

        //JEI
        add("jei.casting.solidifier", "Solidifier");
        add("jei.casting.mixing", "Mixing");
        add("jei.casting.melting", "Melting");
        add("jei.casting.fuel", "Fuels / Coolants");
        add("jei.casting.fuel_temp", "Fuel Temp: %s");
        add("jei.casting.melting_temp", "Melting Temp: %s");



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
