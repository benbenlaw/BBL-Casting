package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.casting.data.custom.MeltingRecipeBuilder;
import com.benbenlaw.casting.data.custom.SolidifierRecipeBuilder;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.fluid.FluidProcessingData;
import com.benbenlaw.casting.fluid.ProcessingType;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.core.fluid.FluidDeferredRegister;
import com.benbenlaw.core.fluid.FluidRegistryObject;
import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.core.tag.ResourceType;
import net.minecraft.core.HolderLookup;
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

public class CastingMeltingRecipeProvider extends RecipeProvider {

    public CastingMeltingRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new CastingMeltingRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return Casting.MOD_ID + " Melting Recipes";
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
        Fluid fluid = CastingFluids.FLUIDS_MAP.get(fluidName).getFluid();

        generateSubRecipe(materialName, type.getResourceType(), baseMb, 1, getMainMold(type), fluid);

        int blockMultiplier = (type == ProcessingType.GEMS4 || type == ProcessingType.DUST4) ? 4 : 9;
        generateSubRecipe(materialName, ResourceType.STORAGE_BLOCKS, baseMb * blockMultiplier, 1, CastingItems.BLOCK_MOLD.get(), fluid);

        generateSubRecipe(materialName, ResourceType.NUGGETS, baseMb / 9, 1, CastingItems.NUGGET_MOLD.get(), fluid);
        generateSubRecipe(materialName, ResourceType.PLATES, baseMb, 1, CastingItems.PLATE_MOLD.get(), fluid);
        generateSubRecipe(materialName, ResourceType.GEARS, baseMb * 4, 1, CastingItems.GEAR_MOLD.get(), fluid);
        generateSubRecipe(materialName, ResourceType.RODS, baseMb, 2, CastingItems.ROD_MOLD.get(), fluid);
        generateSubRecipe(materialName, ResourceType.WIRES, baseMb, 2, CastingItems.WIRE_MOLD.get(), fluid);
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
