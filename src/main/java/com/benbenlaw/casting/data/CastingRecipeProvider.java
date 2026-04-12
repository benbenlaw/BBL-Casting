package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.data.custom.*;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.core.tag.CommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;


public class CastingRecipeProvider extends RecipeProvider {

    public CastingRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new CastingRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return Casting.MOD_ID + " Recipes";
        }
    }

    @Override
    protected void buildRecipes() {

        //Molds
        createMoldRecipe(new ItemStackTemplate(CastingItems.INGOT_MOLD.get()), Tags.Items.INGOTS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.INGOT_MOLD.get()), Tags.Items.BRICKS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.NUGGET_MOLD.get()), Tags.Items.NUGGETS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.GEAR_MOLD.get()), TagKey.create(Registries.ITEM, Identifier.parse("c:gears")));
        createMoldRecipe(new ItemStackTemplate(CastingItems.PLATE_MOLD.get()), TagKey.create(Registries.ITEM, Identifier.parse("c:plates")));
        createMoldRecipe(new ItemStackTemplate(CastingItems.PLATE_MOLD.get()), ItemTags.WOODEN_PRESSURE_PLATES);
        createMoldRecipe(new ItemStackTemplate(CastingItems.ROD_MOLD.get()), Tags.Items.RODS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.BLOCK_MOLD.get()), Tags.Items.STORAGE_BLOCKS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.BLOCK_MOLD.get()), Tags.Items.COBBLESTONES);
        createMoldRecipe(new ItemStackTemplate(CastingItems.BLOCK_MOLD.get()), Tags.Items.STONES);
        createMoldRecipe(new ItemStackTemplate(CastingItems.GEM_MOLD.get()), Tags.Items.GEMS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.DUST_MOLD.get()), Tags.Items.DUSTS);
        createMoldRecipe(new ItemStackTemplate(CastingItems.BALL_MOLD.get()), TagKey.create(Registries.ITEM, Identifier.parse("c:ball_items")));
        createMoldRecipe(new ItemStackTemplate(CastingItems.WIRE_MOLD.get()), TagKey.create(Registries.ITEM, Identifier.parse("c:wires")));

        //Reset
        shapeless(RecipeCategory.MISC, CastingBlocks.CONTROLLER).requires(CastingBlocks.CONTROLLER).unlockedBy("has_controller", has(CastingBlocks.CONTROLLER)).save(output);
        shapeless(RecipeCategory.MISC, CastingBlocks.SOLIDIFIER).requires(CastingBlocks.SOLIDIFIER).unlockedBy("has_solidifier", has(CastingBlocks.SOLIDIFIER)) .save(output);
        shapeless(RecipeCategory.MISC, CastingBlocks.MIXER).requires(CastingBlocks.MIXER).unlockedBy("has_mixer", has(CastingBlocks.MIXER)).save(output);
        shapeless(RecipeCategory.MISC, CastingBlocks.TANK).requires(CastingBlocks.TANK).unlockedBy("has_tank", has(CastingBlocks.TANK)).save(output);

        //Black Bricks
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(Items.CLAY_BALL), RecipeCategory.MISC, CookingBookCategory.MISC, CastingItems.BLACK_BRICK, 0.1f, 100)
                .unlockedBy("has_clay", has(Items.CLAY))
                .save(output, "casting:smelting/black_brick_from_clay");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(Items.CLAY), RecipeCategory.MISC, CookingBookCategory.MISC, CastingBlocks.BLACK_BRICKS, 0.1f, 100)
                .unlockedBy("has_clay", has(Items.CLAY))
                .save(output, "casting:smelting/black_bricks_from_clay_block");

        shaped(RecipeCategory.MISC, CastingBlocks.BLACK_BRICKS, 1).define('#', CastingItems.BLACK_BRICK).pattern("##").pattern("##")
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/black_bricks");

        //Black Brick Glass
        shaped(RecipeCategory.MISC, CastingBlocks.BLACK_BRICK_GLASS, 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', Tags.Items.GLASS_BLOCKS)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/black_brick_glass");

        //Controller
        shaped(RecipeCategory.MISC, CastingBlocks.CONTROLLER)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', CastingBlocks.BLACK_BRICKS)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/controller");

        //Solidifier
        shaped(RecipeCategory.MISC, CastingBlocks.SOLIDIFIER)
                .pattern("AAA")
                .pattern("B B")
                .pattern("AAA")
                .define('A', CastingBlocks.BLACK_BRICKS)
                .define('B', CastingItems.BLACK_BRICK)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/solidifier");

        //Mixer
        shaped(RecipeCategory.MISC, CastingBlocks.MIXER)
                .pattern("AAA")
                .pattern("B B")
                .pattern("AAA")
                .define('A', CastingBlocks.BLACK_BRICKS)
                .define('B', CastingBlocks.BLACK_BRICK_GLASS)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/mixer");

        //Tank
        shaped(RecipeCategory.MISC, CastingBlocks.TANK)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', CastingBlocks.BLACK_BRICK_GLASS)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "casting:crafting/tank");

    }

    public void createMoldRecipe(ItemStackTemplate result, TagKey<Item> ingredient) {

        Identifier tag = ingredient.location();
        String path = tag.getPath();

        shaped(RecipeCategory.MISC, result)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', ingredient)
                .group(Casting.MOD_ID)
                .unlockedBy("has_cobblestone", has(ingredient))
                .save(output.withConditions(new NotCondition(new TagEmptyCondition<>(ingredient))),
                        "casting:molds/" + getItemName(result.item().value()) + "_from_" + path);
    }
}
