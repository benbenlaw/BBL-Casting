package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.casting.data.custom.MeltingRecipeBuilder;
import com.benbenlaw.casting.data.custom.MixingRecipeBuilder;
import com.benbenlaw.casting.data.custom.SolidifierRecipeBuilder;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.fluid.FluidProcessingData;
import com.benbenlaw.casting.fluid.ProcessingType;
import com.benbenlaw.casting.integration.jei.MixingRecipeCategory;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.core.fluid.FluidDeferredRegister;
import com.benbenlaw.core.fluid.FluidRegistryObject;
import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.core.tag.ResourceType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

        //Automatically generate recipes for all fluids based on their processing type
        createBucketRecipe();
        createCommonRecipes();

        //Coal
        simpleSolidifierRecipe(Items.COAL, FluidStackTemplateHelper.getFluidStack("molten_coal", 80),
                CastingItems.GEM_MOLD, "coal/coal");

        //Obsidian
        simpleSolidifierRecipe(Blocks.OBSIDIAN, FluidStackTemplateHelper.getFluidStack("molten_obsidian", 1000),
                CastingItems.BLOCK_MOLD, "obsidian/block");

        //End Stone
        simpleSolidifierRecipe(Blocks.END_STONE, FluidStackTemplateHelper.getFluidStack("molten_end_stone", 1000),
                CastingItems.BLOCK_MOLD, "end_stone/block");

        //Stone
        simpleSolidifierRecipe(Blocks.STONE, FluidStackTemplateHelper.getFluidStack("molten_stone", 1000),
                CastingItems.BLOCK_MOLD, "stone/block");

        //Glass
        simpleSolidifierRecipe(Blocks.GLASS, FluidStackTemplateHelper.getFluidStack("molten_glass", 1000),
                CastingItems.BLOCK_MOLD, "glass/block");

        //Soul
        simpleSolidifierRecipe(Blocks.SOUL_SAND, FluidStackTemplateHelper.getFluidStack("molten_soul", 1000),
                CastingItems.BLOCK_MOLD, "soul/block");

        //Ender
        simpleSolidifierRecipe(Items.ENDER_PEARL, FluidStackTemplateHelper.getFluidStack("molten_ender", 250),
                CastingItems.BALL_MOLD, "ender/pearl");

        //Silicon
        simpleSolidifierRecipe(Items.SAND, FluidStackTemplateHelper.getFluidStack("molten_silicon", 250),
                CastingItems.BALL_MOLD, "silicon/silicon");

        //Bottle o ' Enchanting
        simpleSolidifierRecipe(Items.EXPERIENCE_BOTTLE, FluidStackTemplateHelper.getFluidStack("molten_experience", 250),
                CastingItems.BALL_MOLD, "experience/bottle");

        //Alloys
        //Bronze
        alloyMixingRecipes("bronze", FluidStackTemplateHelper.getFluidStack("molten_bronze", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_tin", 90)));

        //Invar
        alloyMixingRecipes("invar", FluidStackTemplateHelper.getFluidStack("molten_invar", 270),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 180),
                        FluidStackTemplateHelper.getFluidStack("molten_nickel", 90)));

        //Brass
        alloyMixingRecipes("brass", FluidStackTemplateHelper.getFluidStack("molten_brass", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_zinc", 90)));

        //Molten Fluix
        alloyMixingRecipes("fluix", FluidStackTemplateHelper.getFluidStack("molten_fluix", 500),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_certus_quartz", 250),
                        FluidStackTemplateHelper.getFluidStack("molten_charged_certus_quartz", 250),
                        FluidStackTemplateHelper.getFluidStack("molten_redstone", 90)));

        //Enderium
        alloyMixingRecipes("enderium", FluidStackTemplateHelper.getFluidStack("molten_enderium", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_lead", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_platinum", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_ender", 500)));

        //Pulsating Alloy
        alloyMixingRecipes("pulsating_iron", FluidStackTemplateHelper.getFluidStack("molten_pulsating_alloy", 180),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_ender", 250),
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 90)));

        //End Steel
        alloyMixingRecipes("end_steel", FluidStackTemplateHelper.getFluidStack("molten_end_steel", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_end_stone", 1000),
                        FluidStackTemplateHelper.getFluidStack("molten_dark_steel", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_obsidian", 1000)));

        //Electrum
        alloyMixingRecipes("electrum", FluidStackTemplateHelper.getFluidStack("molten_electrum", 180),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_gold", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_silver", 90)));

        //Quartz Enriched Iron
        alloyMixingRecipes("quartz_enriched_iron", FluidStackTemplateHelper.getFluidStack("molten_quartz_enriched_iron", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_quartz", 250)));

        //Conductive Alloy
        alloyMixingRecipes("conductive_alloy", FluidStackTemplateHelper.getFluidStack("molten_conductive_alloy", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 90)));

        //Vibrant Alloy
        alloyMixingRecipes("vibrant_alloy", FluidStackTemplateHelper.getFluidStack("molten_vibrant_alloy", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_ender", 250),
                        FluidStackTemplateHelper.getFluidStack("molten_energetic_alloy", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_glowstone", 250)));

        //Signalum
        alloyMixingRecipes("signalum", FluidStackTemplateHelper.getFluidStack("molten_signalum", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_silver", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_redstone", 360)));

        //Quartz Enriched Copper
        alloyMixingRecipes("quartz_enriched_copper", FluidStackTemplateHelper.getFluidStack("molten_quartz_enriched_copper", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_quartz", 250)));

        //Steel
        alloyMixingRecipes("steel", FluidStackTemplateHelper.getFluidStack("molten_steel", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_coal", 160)));

        //Soularium
        alloyMixingRecipes("soularium", FluidStackTemplateHelper.getFluidStack("molten_soularium", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_soul", 1000),
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 90)));

        //Dark Steel
        alloyMixingRecipes("dark_steel", FluidStackTemplateHelper.getFluidStack("molten_dark_steel", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_iron", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_coal", 80),
                        FluidStackTemplateHelper.getFluidStack("molten_obsidian", 1000)));

        //Lumium
        alloyMixingRecipes("lumium", FluidStackTemplateHelper.getFluidStack("molten_lumium", 360),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_tin", 270),
                        FluidStackTemplateHelper.getFluidStack("molten_silver", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_glowstone", 500)));

        //Constantan
        alloyMixingRecipes("constantan", FluidStackTemplateHelper.getFluidStack("molten_constantan", 180),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_copper", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_nickel", 90)));

        //Energetic Alloy
        alloyMixingRecipes("energetic_alloy", FluidStackTemplateHelper.getFluidStack("molten_energetic_alloy", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_redstone", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_gold", 90),
                        FluidStackTemplateHelper.getFluidStack("molten_conductive_alloy", 90)));

        //Obsidian
        alloyMixingRecipes("obsidian", FluidStackTemplateHelper.getFluidStack("molten_obsidian", 1000),
                List.of(
                        new FluidStackTemplate(Fluids.LAVA, 1000),
                        new FluidStackTemplate(Fluids.WATER, 1000)));

        //Netherite
        alloyMixingRecipes("netherite", FluidStackTemplateHelper.getFluidStack("molten_netherite", 90),
                List.of(
                        FluidStackTemplateHelper.getFluidStack("molten_debris", 360),
                        FluidStackTemplateHelper.getFluidStack("molten_gold", 360)));

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
        int temp = processing.temp(); // Get the temperature from your data
        Fluid fluid = CastingFluids.FLUIDS_MAP.get(fluidName).getFluid();

        Item mold = getMainMold(type);
        generateSubRecipe(materialName, type.getResourceType(), baseMb, 1, mold, fluid);
        generateMeltingRecipe(materialName, type.getResourceType(), baseMb, 1, fluid, temp);

        int blockMultiplier = (type == ProcessingType.GEMS4 || type == ProcessingType.DUST4) ? 4 : 9;
        generateSubRecipe(materialName, ResourceType.STORAGE_BLOCKS, baseMb * blockMultiplier, 1, CastingItems.BLOCK_MOLD.get(), fluid);
        generateMeltingRecipe(materialName, ResourceType.STORAGE_BLOCKS, baseMb * blockMultiplier, 1, fluid, temp);
        processBoth(materialName, ResourceType.NUGGETS, baseMb / 9, 1, CastingItems.NUGGET_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.PLATES, baseMb, 1, CastingItems.PLATE_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.GEARS, baseMb * 4, 1, CastingItems.GEAR_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.RODS, baseMb / 2, 1, CastingItems.ROD_MOLD.get(), fluid, temp);
        processBoth(materialName, ResourceType.WIRES, baseMb / 2, 1, CastingItems.WIRE_MOLD.get(), fluid, temp);

        //Melting Ores and Raw Materials
        int oreAmount = (int) (baseMb * 1.5);
        generateOreMeltingRecipe(materialName, ResourceType.ORES, oreAmount, fluid, temp, "ores");
        generateOreMeltingRecipe(materialName, ResourceType.RAW_MATERIALS, oreAmount, fluid, temp, "raw_materials");
    }

    private void processBoth(String mat, ResourceType res, int mb, int count, Item mold, Fluid f, int t) {
        generateSubRecipe(mat, res, mb, count, mold, f);
        generateMeltingRecipe(mat, res, mb, 1, f, t);
    }

    private void generateSubRecipe(String material, ResourceType resourceType, int fluidAmount, int outputCount, Item mold, Fluid fluid) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);

        String typePath = switch (resourceType) {
            case STORAGE_BLOCKS -> "block";
            case DUSTS -> "dust";
            case GEMS -> "gem";
            case INGOTS -> "ingot";
            case NUGGETS -> "nugget";
            case PLATES -> "plate";
            case GEARS -> "gear";
            case RODS -> "rod";
            case WIRES -> "wire";
            default -> resourceType.name().toLowerCase(Locale.ROOT);
        };

        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                        SizedIngredient.of(mold, 1),
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), outputCount),
                        new FluidStackTemplate(fluid, fluidAmount))
                .unlockedBy("has_mold", has(mold))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + typePath);

    }

    private void generateMeltingRecipe(String material, ResourceType resourceType, int fluidAmount, int inputCount, Fluid fluid, int temp) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);
        String typePath = getCleanPath(resourceType);

        MeltingRecipeBuilder.meltingRecipesBuilder(
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), inputCount),
                        List.of(new FluidStackTemplate(fluid, fluidAmount)),
                        temp)
                .unlockedBy("has_" + typePath, has(tag))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + typePath);
    }

    private void generateOreMeltingRecipe(String material, ResourceType resourceType, int fluidAmount, Fluid fluid, int temp, String idSuffix) {
        TagKey<Item> tag = CommonTags.getItemTag(resourceType, material);

        MeltingRecipeBuilder.meltingRecipesBuilder(
                        new SizedIngredient(Ingredient.of(tag(tag).getValues()), 1),
                        List.of(new FluidStackTemplate(fluid, fluidAmount), FluidStackTemplateHelper.getFluidStack("molten_experience", 10)),
                        temp)
                .unlockedBy("has_" + idSuffix, has(tag))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(tag))),
                        material + "/" + idSuffix);
    }

    private void alloyMixingRecipes(String material, FluidStackTemplate outputFluid, List<FluidStackTemplate> inputFluids) {

        NonNullList<FluidStackTemplate> inputs = NonNullList.create();
        inputs.addAll(inputFluids);

        MixingRecipeBuilder.mixingRecipesBuilder(
                        inputs,
                        outputFluid)
                .unlockedBy("has_copper_ingot", has(CastingBlocks.MIXER))
                .save(output, material + "alloy");
    }

    private String getCleanPath(ResourceType resourceType) {
        return switch (resourceType) {
            case STORAGE_BLOCKS -> "block";
            case DUSTS -> "dust";
            case INGOTS -> "ingot";
            case GEMS -> "gem";
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

    public void createBucketRecipe() {

        for (Map.Entry<String, FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes,
                BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem>> map : CastingFluids.FLUIDS_MAP.entrySet()) {

            ItemStackTemplate bucket = new ItemStackTemplate(map.getValue().getBucket());
            Fluid fluid = map.getValue().getFluid();

            SolidifierRecipeBuilder.solidifierRecipesBuilder(
                    SizedIngredient.of(Items.BUCKET, 1),
                    SizedIngredient.of(bucket.item().value(), 1),
                    new FluidStackTemplate(fluid, 1000)).save(output, "buckets/" + map.getKey());
        }
    }

    public void simpleSolidifierRecipe(ItemLike block, FluidStackTemplate fluidStack, ItemLike mold, String id) {
        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                SizedIngredient.of(mold, 1),
                SizedIngredient.of(block.asItem(), 1),
                fluidStack).save(output, id);
    }
}
