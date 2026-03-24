package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.custom.*;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.core.tag.CommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

import static com.benbenlaw.casting.data.custom.FluidStackHelper.fluidList;
import static com.benbenlaw.casting.data.custom.FluidStackHelper.getFluidStack;
import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;

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

    }
}
