package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.custom.FuelRecipeBuilder;
import com.benbenlaw.casting.data.custom.MeltingRecipeBuilder;
import com.benbenlaw.casting.data.custom.MixingRecipeBuilder;
import com.benbenlaw.casting.data.custom.SolidifierRecipeBuilder;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.fluid.FluidProcessingData;
import com.benbenlaw.casting.fluid.ProcessingType;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.core.tag.ResourceType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.casting.data.custom.FluidStackTemplateHelper.*;

public class CastingProcessingRecipeProvider extends RecipeProvider {

    public CastingProcessingRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new CastingProcessingRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return Casting.MOD_ID + " Processing Recipes";
        }
    }

    @Override
    protected void buildRecipes() {

        createCommonRecipes();

        //Mud
        simpleSolidifierRecipe(Items.MUD, new SizedFluidIngredient(FluidIngredient.of(Fluids.WATER), 250),
                Items.DIRT, "dirt/mud", ResourceType.STORAGE_BLOCKS, 600);

        //Coal
        simpleSolidifierRecipe(Items.COAL, getFluidIngredient("molten_coal", 80),
                CastingItems.GEM_MOLD, "coal/coal", ResourceType.GEMS, getTempFromFluid("molten_coal"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_coal", 80)), Items.COAL,
                "coal/coal", ResourceType.INGOTS, getTempFromFluid("molten_coal"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_coal", 720)), Blocks.COAL_BLOCK,
                "coal/coal_block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_coal"));

        //Blaze
        simpleSolidifierRecipe(Items.BLAZE_POWDER, getFluidIngredient("molten_blaze", 90),
                CastingItems.DUST_MOLD, "blaze/blaze_powder", ResourceType.DUSTS, getTempFromFluid("molten_blaze"));

        simpleSolidifierRecipe(Items.BLAZE_ROD, getFluidIngredient("molten_blaze", 180),
                CastingItems.ROD_MOLD, "blaze/blaze_rod", ResourceType.RODS, getTempFromFluid("molten_blaze"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_blaze", 45)), Items.BLAZE_POWDER,
                "blaze/blaze_powder", ResourceType.DUSTS, 1200);

        simpleMeltingRecipe(List.of(getFluidStack("molten_blaze", 90)), Items.BLAZE_ROD,
                "blaze/blaze_rod", ResourceType.RODS, getTempFromFluid("molten_blaze"));

        //Ancient Debris
        simpleMeltingRecipe(List.of(getFluidStack("molten_debris", 90)), Items.ANCIENT_DEBRIS,
                "debris/ancient_debris", ResourceType.ORES, getTempFromFluid("molten_debris"));

        //Obsidian
        simpleMeltingRecipe(List.of(getFluidStack("molten_obsidian", 1000)), Blocks.OBSIDIAN,
                "obsidian/obsidian", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_obsidian"));

        simpleSolidifierRecipe(Blocks.OBSIDIAN, getFluidIngredient("molten_obsidian", 1000),
                CastingItems.BLOCK_MOLD, "obsidian/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_obsidian"));

        //End Stone
        simpleMeltingRecipe(List.of(getFluidStack("molten_end_stone", 1000)), Blocks.END_STONE,
                "end_stone/end_stone", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_end_stone"));

        simpleSolidifierRecipe(Blocks.END_STONE, getFluidIngredient("molten_end_stone", 1000),
                CastingItems.BLOCK_MOLD, "end_stone/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_end_stone"));

        //Stone
        simpleMeltingRecipe(List.of(getFluidStack("molten_stone", 1000)), tag(Tags.Items.STONES),
                "stone/stone", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_stone"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_stone", 1000)), tag(Tags.Items.COBBLESTONES),
                "stone/cobblestone", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_stone"));;

        simpleSolidifierRecipe(Blocks.STONE, getFluidIngredient("molten_stone", 1000),
                CastingItems.BLOCK_MOLD, "stone/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_stone"));

        //Glass
        simpleMeltingRecipe(List.of(getFluidStack("molten_glass", 1000)), tag(Tags.Items.SANDS),
                "glass/sand", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_stone"));;

        simpleSolidifierRecipe(Blocks.GLASS, getFluidIngredient("molten_glass", 1000),
                CastingItems.BLOCK_MOLD, "glass/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_glass"));

        //Soul
        simpleMeltingRecipe(List.of(getFluidStack("molten_soul", 1000)), tag(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                "soul/soul_sand", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_soul"));

        simpleSolidifierRecipe(Blocks.SOUL_SAND, getFluidIngredient("molten_soul", 1000),
                CastingItems.BLOCK_MOLD, "soul/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_soul"));

        //Ender
        simpleMeltingRecipe(List.of(getFluidStack("molten_ender", 250)), Items.ENDER_PEARL,
                "ender/pearl", ResourceType.GEMS, getTempFromFluid("molten_ender"));

        simpleSolidifierRecipe(Items.ENDER_PEARL, getFluidIngredient("molten_ender", 250),
                CastingItems.BALL_MOLD, "ender/pearl", ResourceType.GEMS, getTempFromFluid("molten_ender"));

        //Silicon
        TagKey<Item> siliconTag = TagKey.create(Registries.ITEM, Identifier.parse("c:silicon"));

        SolidifierRecipeBuilder.solidifierRecipesBuilder(SizedIngredient.of(CastingItems.BALL_MOLD.asItem(), 1), new SizedIngredient(tag(siliconTag), 1), getFluidIngredient("molten_silicon", 250),
                getTempFromFluid("molten_silicon"), Optional.empty()).save(output.withConditions(new NotCondition(new TagEmptyCondition<>(siliconTag))), "silicon/silicon");

        List<FluidStackTemplate> silicon = new ArrayList<>();
        silicon.add(new FluidStackTemplate(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_silicon")), 250));

        MeltingRecipeBuilder.meltingRecipesBuilder(new SizedIngredient(tag(siliconTag), 1), silicon,
                getTempFromFluid("molten_silicon"), Optional.empty()).save(output.withConditions(new NotCondition(new TagEmptyCondition<>(siliconTag))), "silicon/silicon");


        //Experience
        simpleSolidifierRecipe(CastingItems.EXPERIENCE_BALL, getFluidIngredient("molten_experience", 1000),
                CastingItems.BALL_MOLD, "experience/ball", ResourceType.GEMS, getTempFromFluid("molten_experience"));

        simpleSolidifierRecipe(Items.EXPERIENCE_BOTTLE, getFluidIngredient("molten_experience", 1000),
                Items.GLASS_BOTTLE, "experience/bottle", ResourceType.GEMS, getTempFromFluid("molten_experience"));

        //Black Bricks
        simpleSolidifierRecipe(CastingBlocks.BLACK_BRICKS.get(), getFluidIngredient("molten_black_brick", 1000),
                CastingItems.BLOCK_MOLD, "black_brick/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_black_brick"));

        simpleSolidifierRecipe(CastingItems.BLACK_BRICK.get(), getFluidIngredient("molten_black_brick", 250),
                CastingItems.INGOT_MOLD, "black_brick/brick", ResourceType.INGOTS, getTempFromFluid("molten_black_brick"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_black_brick", 1000)), CastingBlocks.BLACK_BRICKS.get(),
                "black_brick/block", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_black_brick"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_black_brick", 250)), CastingItems.BLACK_BRICK.get(),
                "black_brick/brick", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_black_brick"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_black_brick", 1000)), Blocks.CLAY,
                "black_brick/clay", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_black_brick"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_black_brick", 250)), Items.CLAY_BALL,
                "black_brick/clay_ball", ResourceType.STORAGE_BLOCKS, getTempFromFluid("molten_black_brick"));

        //Amethyst
        simpleSolidifierRecipe(Items.AMETHYST_SHARD, getFluidIngredient("molten_amethyst", 90),
                CastingItems.SHARD_MOLD, "amethyst/shard", ResourceType.SHARDS, getTempFromFluid("molten_amethyst"));

        simpleMeltingRecipe(List.of(getFluidStack("molten_amethyst", 90)), Items.AMETHYST_SHARD,
                "amethyst/shard", ResourceType.SHARDS, getTempFromFluid("molten_amethyst"));

        //Alloys
        //Bronze
        alloyMixingRecipes("bronze", getFluidStack("molten_bronze", 360),
                List.of(
                        getFluidIngredient("molten_copper", 270),
                        getFluidIngredient("molten_tin", 90)));

        //Invar
        alloyMixingRecipes("invar", getFluidStack("molten_invar", 270),
                List.of(
                        getFluidIngredient("molten_iron", 180),
                        getFluidIngredient("molten_nickel", 90)));

        //Brass
        alloyMixingRecipes("brass", getFluidStack("molten_brass", 360),
                List.of(
                        getFluidIngredient("molten_copper", 270),
                        getFluidIngredient("molten_zinc", 90)));

        //Molten Fluix
        alloyMixingRecipes("fluix", getFluidStack("molten_fluix", 500),
                List.of(
                        getFluidIngredient("molten_certus_quartz", 250),
                        getFluidIngredient("molten_charged_certus_quartz", 250),
                        getFluidIngredient("molten_redstone", 90)));

        //Enderium
        alloyMixingRecipes("enderium", getFluidStack("molten_enderium", 360),
                List.of(
                        getFluidIngredient("molten_lead", 270),
                        getFluidIngredient("molten_platinum", 90),
                        getFluidIngredient("molten_ender", 500)));

        //Pulsating Alloy
        alloyMixingRecipes("pulsating_iron", getFluidStack("molten_pulsating_alloy", 180),
                List.of(
                        getFluidIngredient("molten_ender", 250),
                        getFluidIngredient("molten_iron", 90)));

        //End Steel
        alloyMixingRecipes("end_steel", getFluidStack("molten_end_steel", 90),
                List.of(
                        getFluidIngredient("molten_end_stone", 1000),
                        getFluidIngredient("molten_dark_steel", 90),
                        getFluidIngredient("molten_obsidian", 1000)));

        //Electrum
        alloyMixingRecipes("electrum", getFluidStack("molten_electrum", 180),
                List.of(
                        getFluidIngredient("molten_gold", 90),
                        getFluidIngredient("molten_silver", 90)));

        //Quartz Enriched Iron
        alloyMixingRecipes("quartz_enriched_iron", getFluidStack("molten_quartz_enriched_iron", 360),
                List.of(
                        getFluidIngredient("molten_iron", 270),
                        getFluidIngredient("molten_quartz", 250)));

        //Conductive Alloy
        alloyMixingRecipes("conductive_alloy", getFluidStack("molten_conductive_alloy", 360),
                List.of(
                        getFluidIngredient("molten_copper", 90),
                        getFluidIngredient("molten_iron", 90)));

        //Vibrant Alloy
        alloyMixingRecipes("vibrant_alloy", getFluidStack("molten_vibrant_alloy", 90),
                List.of(
                        getFluidIngredient("molten_ender", 250),
                        getFluidIngredient("molten_energetic_alloy", 90),
                        getFluidIngredient("molten_glowstone", 250)));

        //Redstone Alloy
        alloyMixingRecipes("redstone_alloy", getFluidStack("molten_redstone_alloy", 90),
                List.of(
                        getFluidIngredient("molten_redstone", 90),
                        getFluidIngredient("molten_copper", 90)));

        //Signalum
        alloyMixingRecipes("signalum", getFluidStack("molten_signalum", 360),
                List.of(
                        getFluidIngredient("molten_copper", 270),
                        getFluidIngredient("molten_silver", 90),
                        getFluidIngredient("molten_redstone", 360)));

        //Quartz Enriched Copper
        alloyMixingRecipes("quartz_enriched_copper", getFluidStack("molten_quartz_enriched_copper", 360),
                List.of(
                        getFluidIngredient("molten_copper", 270),
                        getFluidIngredient("molten_quartz", 250)));

        //Steel
        alloyMixingRecipes("steel", getFluidStack("molten_steel", 90),
                List.of(
                        getFluidIngredient("molten_iron", 90),
                        getFluidIngredient("molten_coal", 160)));

        //Soularium
        alloyMixingRecipes("soularium", getFluidStack("molten_soularium", 90),
                List.of(
                        getFluidIngredient("molten_soul", 1000),
                        getFluidIngredient("molten_iron", 90)));

        //Dark Steel
        alloyMixingRecipes("dark_steel", getFluidStack("molten_dark_steel", 90),
                List.of(
                        getFluidIngredient("molten_iron", 90),
                        getFluidIngredient("molten_coal", 80),
                        getFluidIngredient("molten_obsidian", 1000)));

        //Lumium
        alloyMixingRecipes("lumium", getFluidStack("molten_lumium", 360),
                List.of(
                        getFluidIngredient("molten_tin", 270),
                        getFluidIngredient("molten_silver", 90),
                        getFluidIngredient("molten_glowstone", 500)));

        //Constantan
        alloyMixingRecipes("constantan", getFluidStack("molten_constantan", 180),
                List.of(
                        getFluidIngredient("molten_copper", 90),
                        getFluidIngredient("molten_nickel", 90)));

        //Energetic Alloy
        alloyMixingRecipes("energetic_alloy", getFluidStack("molten_energetic_alloy", 90),
                List.of(
                        getFluidIngredient("molten_redstone", 90),
                        getFluidIngredient("molten_gold", 90),
                        getFluidIngredient("molten_conductive_alloy", 90)));

        //Obsidian
        alloyMixingRecipes("obsidian", getFluidStack("molten_obsidian", 1000),
                List.of(
                        new SizedFluidIngredient(FluidIngredient.of(Fluids.LAVA), 1000),
                        new SizedFluidIngredient(FluidIngredient.of(Fluids.WATER), 1000)
                ));

        //Netherite
        alloyMixingRecipes("netherite", getFluidStack("molten_netherite", 90),
                List.of(
                        getFluidIngredient("molten_debris", 360),
                        getFluidIngredient("molten_gold", 360)));

        //Scorchium
        alloyMixingRecipes("scorchium", getFluidStack("scorchium", 360),
                List.of(
                        getFluidIngredient("blazing_lava", 4000),
                        getFluidIngredient("molten_obsidian", 1000),
                        getFluidIngredient("molten_coal", 320)));

        //Blazing Lava
        alloyMixingRecipes("blazing_lava", getFluidStack("blazing_lava", 1000),
                List.of(
                        new SizedFluidIngredient(FluidIngredient.of(Fluids.LAVA), 1000),
                        getFluidIngredient("molten_blaze", 360)));

        //Glacium
        alloyMixingRecipes("glacium", getFluidStack("glacium", 1000),
                List.of(
                        getFluidIngredient("super_coolant", 1000),
                        getFluidIngredient("chilled_water", 500),
                        getFluidIngredient("iced_water", 500)));
        //Super Coolant
        alloyMixingRecipes("super_coolant", getFluidStack("super_coolant", 1000),
                List.of(
                        getFluidIngredient("chilled_water", 500),
                        getFluidIngredient("iced_water", 500)));

        //Chilled Water
        simpleMeltingRecipe(List.of(getFluidStack("chilled_water", 1000)), Items.SNOW_BLOCK,
                "chilled_water/snow", ResourceType.STORAGE_BLOCKS, getTempFromFluid("chilled_water"));

        simpleMeltingRecipe(List.of(getFluidStack("chilled_water", 250)), Items.SNOW,
                "chilled_water/snow_ball", ResourceType.DUSTS, getTempFromFluid("chilled_water"));

        //Iced Water
        simpleMeltingRecipe(List.of(getFluidStack("iced_water", 1000)), Items.ICE,
                "iced_water/ice", ResourceType.STORAGE_BLOCKS, getTempFromFluid("iced_water"));

        simpleMeltingRecipe(List.of(getFluidStack("iced_water", 9000)), Items.PACKED_ICE,
                "iced_water/packed_ice", ResourceType.STORAGE_BLOCKS, getTempFromFluid("iced_water"));

        simpleMeltingRecipe(List.of(getFluidStack("iced_water", 81000)), Items.BLUE_ICE,
                "iced_water/blue_ice", ResourceType.STORAGE_BLOCKS, getTempFromFluid("iced_water"));


        //Fuels - Heat
        FuelRecipeBuilder.fuelRecipesBuilder(new SizedFluidIngredient(FluidIngredient.of(Fluids.LAVA), 25), 1000).save(output, "lava");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("molten_obsidian", 20), getTempFromFluid("molten_obsidian")).save(output, "obsidian");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("molten_coal", 25), getTempFromFluid("molten_coal")).save(output, "coal");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("molten_uranium", 15), getTempFromFluid("molten_uranium")).save(output, "uranium");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("molten_blaze", 25), getTempFromFluid("molten_blaze")).save(output, "blaze");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("blazing_lava", 50), getTempFromFluid("blazing_lava")).save(output, "blazing_lava");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("molten_blazing_crystal", 50), getTempFromFluid("molten_blazing_crystal")).save(output, "molten_blazing_crystal");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("scorchium", 10), getTempFromFluid("scorchium")).save(output, "scorchium");

        //Fuels - Cool
        FuelRecipeBuilder.fuelRecipesBuilder(new SizedFluidIngredient(FluidIngredient.of(Fluids.WATER), 60), 600).save(output, "water");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("chilled_water", 45), getTempFromFluid("chilled_water")).save(output, "chilled_water");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("iced_water", 35), getTempFromFluid("iced_water")).save(output, "iced_water");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("super_coolant", 20), getTempFromFluid("super_coolant")).save(output, "super_coolant");
        FuelRecipeBuilder.fuelRecipesBuilder(getFluidIngredient("glacium", 10), getTempFromFluid("glacium")).save(output, "glacium");
    }

    public void createCommonRecipes() {
        for (FluidData data : FluidData.FLUID_DEFINITIONS) {
            FluidProcessingData processing = data.fluidProduceType();
            ProcessingType type = processing.resourceType();

            if (type == ProcessingType.CUSTOM || type.getResourceType() == null) {
                continue;
            }
            generateAutomaticRecipe(data);
        }
    }

    private void generateAutomaticRecipe(FluidData data) {
        String fluidName = data.name();
        String materialName = fluidName.replace("molten_", "");
        FluidProcessingData processing = data.fluidProduceType();
        ProcessingType type = processing.resourceType();

        int baseMb = processing.mbPerSingleItem();
        int temp = processing.temp();
        Fluid fluid = CastingFluids.FLUIDS_MAP.get(fluidName).getFluid();

        Item mold = getMainMold(type);
        generateSubRecipe(materialName, type.getResourceType(), baseMb, 1, mold, fluid, temp);
        generateMeltingRecipe(materialName, type.getResourceType(), baseMb, 1, fluid, temp);

        int blockMultiplier = (type == ProcessingType.GEMS4 || type == ProcessingType.DUST4) ? 4 : 9;
        generateSubRecipe(materialName, ResourceType.STORAGE_BLOCKS, baseMb * blockMultiplier, 1, CastingItems.BLOCK_MOLD.get(), fluid, temp);
        generateMeltingRecipe(materialName, ResourceType.STORAGE_BLOCKS, baseMb * blockMultiplier, 1, fluid, temp);
        processBoth(materialName, ResourceType.NUGGETS, baseMb / 9, 1, CastingItems.NUGGET_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.PLATES, baseMb, 1, CastingItems.PLATE_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.GEARS, baseMb * 4, 1, CastingItems.GEAR_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.RODS, baseMb / 2, 1, CastingItems.ROD_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.WIRES, baseMb / 2, 1, CastingItems.WIRE_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.SHARDS, baseMb, 1, CastingItems.SHARD_MOLD.get(), fluid, temp);

        if (type.getResourceType() != ResourceType.DUSTS) {
            processBoth(materialName, ResourceType.DUSTS, baseMb, 1, CastingItems.DUST_MOLD.get(), fluid, temp);
        }

        int oreAmount = (int) (baseMb * 1.5);
        generateOreMeltingRecipe(materialName, ResourceType.ORES, oreAmount, 10, fluid, temp, "ores");
        generateOreMeltingRecipe(materialName, ResourceType.RAW_MATERIALS, oreAmount, 10, fluid, temp, "raw_materials");
        generateOreMeltingRecipe(materialName, ResourceType.RAW_STORAGE_BLOCKS, oreAmount * 9, 90, fluid, temp, "raw_block");
    }

    private void processBoth(String mat, ResourceType res, int mb, int count, Item mold, Fluid f, int t) {
        generateSubRecipe(mat, res, mb, count, mold, f, t);
        generateMeltingRecipe(mat, res, mb, 1, f, t);
    }

    private void generateSubRecipe(String material, ResourceType resourceType, int fluidAmount, int outputCount, Item mold, Fluid fluid, int temp) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);

        if (resourceType == ResourceType.SHARDS) {
            tag = TagKey.create(Registries.ITEM, Identifier.parse("geore:geore_shards/" + material));
        }

        String typePath = switch (resourceType) {
            case STORAGE_BLOCKS -> "block";
            case RAW_STORAGE_BLOCKS -> "raw_block";
            case DUSTS -> "dust";
            case GEMS -> "gem";
            case INGOTS -> "ingot";
            case NUGGETS -> "nugget";
            case PLATES -> "plate";
            case GEARS -> "gear";
            case RODS -> "rod";
            case WIRES -> "wire";
            case SHARDS -> "shard";
            default -> resourceType.name().toLowerCase(Locale.ROOT);
        };


        HolderSet<Fluid> fluidTagSet = getFluidTag(material);
        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                        SizedIngredient.of(mold, 1),
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), outputCount),
                        getFluidTagIngredient(fluidTagSet, fluidAmount),
                        temp,
                        Optional.of(getDurationModifier(resourceType)))
                .unlockedBy("has_mold", has(mold))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + typePath);

    }

    private void generateMeltingRecipe(String material, ResourceType resourceType, int fluidAmount, int inputCount, Fluid fluid, int temp) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);

        if (resourceType == ResourceType.SHARDS) {
            tag = TagKey.create(Registries.ITEM, Identifier.parse("geore:geore_shards/" + material));
        }

        String typePath = getCleanPath(resourceType);

        MeltingRecipeBuilder.meltingRecipesBuilder(
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), inputCount),
                        List.of(new FluidStackTemplate(fluid, fluidAmount)),
                        temp, Optional.of(getDurationModifier(resourceType)))
                .unlockedBy("has_" + typePath, has(tag))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + typePath);
    }

    private void generateOreMeltingRecipe(String material, ResourceType resourceType, int fluidAmount, int xp, Fluid fluid, int temp, String idSuffix) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);

        if (resourceType == ResourceType.RAW_STORAGE_BLOCKS) {
            tag = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "storage_blocks/raw_" + material));
        }

        MeltingRecipeBuilder.meltingRecipesBuilder(
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), 1),
                        List.of(new FluidStackTemplate(fluid, fluidAmount), getFluidStack("molten_experience", xp)),
                        temp, Optional.of(getDurationModifier(resourceType)))
                .unlockedBy("has_" + idSuffix, has(tag))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + idSuffix);
    }
    private void alloyMixingRecipes(String material, FluidStackTemplate outputFluid, List<SizedFluidIngredient> inputFluids) {

        NonNullList<SizedFluidIngredient> inputs = NonNullList.create();
        inputs.addAll(inputFluids);

        MixingRecipeBuilder.mixingRecipesBuilder(
                        inputs,
                        outputFluid)
                .unlockedBy("has_mixer", has(CastingBlocks.MIXER))
                .save(output, material + "alloy");
    }

    public void simpleSolidifierRecipe(ItemLike block, SizedFluidIngredient fluidStack, ItemLike mold, String id, ResourceType resourceType, int temp) {
        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                SizedIngredient.of(mold, 1),
                SizedIngredient.of(block.asItem(), 1),
                fluidStack,
                temp,
                Optional.of(getDurationModifier(resourceType))).save(output, id);
    }

    public void simpleMeltingRecipe(List<FluidStackTemplate> outputs, ItemLike input, String id, ResourceType resourceType, int temp) {
        MeltingRecipeBuilder.meltingRecipesBuilder(
                SizedIngredient.of(input, 1),
                outputs,
                temp,
                Optional.of(getDurationModifier(resourceType))).save(output, id);
    }

    public void simpleMeltingRecipe(List<FluidStackTemplate> outputs, Ingredient input, String id, ResourceType resourceType, int temp) {
        MeltingRecipeBuilder.meltingRecipesBuilder(
                new SizedIngredient(input, 1),
                outputs,
                temp,
                Optional.of(getDurationModifier(resourceType))).save(output, id);
    }

    public int getTempFromFluid(String fluidName) {
        return FluidData.FLUID_DEFINITIONS.stream().filter(data -> data.name().equals(fluidName)).findFirst().orElseThrow().fluidProduceType().temp();
    }


    private String getCleanPath(ResourceType resourceType) {
        return switch (resourceType) {
            case STORAGE_BLOCKS -> "block";
            case DUSTS -> "dust";
            case INGOTS -> "ingot";
            case GEMS -> "gem";
            case SHARDS -> "shard";
            default -> resourceType.name().toLowerCase(Locale.ROOT).replaceAll("s$", "");
        };
    }

    private Item getMainMold(ProcessingType type) {
        return switch (type) {
            case GEMS4, GEMS9 -> CastingItems.GEM_MOLD.get();
            case DUST4, DUST9 -> CastingItems.DUST_MOLD.get();
            default -> CastingItems.INGOT_MOLD.get();
        };
    }

    private double getDurationModifier(ResourceType type) {
        return switch (type) {
            case NUGGETS -> 0.2;
            case RODS, WIRES -> 0.4;
            case INGOTS, PLATES, DUSTS, GEMS, SHARDS -> 0.5;
            case GEARS -> 1.2;
            case STORAGE_BLOCKS -> 2.5;
            case ORES -> 1.5;
            case RAW_MATERIALS -> 1.25;
            case RAW_STORAGE_BLOCKS -> 3.0;
            default -> 1.0;
        };
    }

    private HolderSet<Fluid> getFluidTag(String material) {
        TagKey<Fluid> tagKey = TagKey.create(Registries.FLUID,
                Identifier.fromNamespaceAndPath("c", "molten_" + material));

        return this.registries.lookupOrThrow(Registries.FLUID)
                .getOrThrow(tagKey);
    }
}
