package com.benbenlaw.casting.event.client;

import com.benbenlaw.casting.recipe.custom.FuelRecipe;
import com.benbenlaw.casting.recipe.custom.MeltingRecipe;
import com.benbenlaw.casting.recipe.custom.MixingRecipe;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientRecipeCache {

    //Melting Recipes
    public static Map<Identifier, MeltingRecipe> cachedMeltingRecipes = new HashMap<>();

    public static void setCachedMeltingRecipes(Map<Identifier, MeltingRecipe> cachedMeltingRecipes) {
        ClientRecipeCache.cachedMeltingRecipes = cachedMeltingRecipes;
    }

    public static Collection<MeltingRecipe> getCachedMeltingRecipes() {
        return cachedMeltingRecipes.values();
    }

    //Solidifier Recipes
    public static Map<Identifier, SolidifierRecipe> cachedSolidifierRecipes = new HashMap<>();

    public static void setCachedSolidifierRecipes(Map<Identifier, SolidifierRecipe> cachedSolidifierRecipes) {
        ClientRecipeCache.cachedSolidifierRecipes = cachedSolidifierRecipes;
    }

    public static Collection<SolidifierRecipe> getCachedSolidifierRecipes() {
        return cachedSolidifierRecipes.values();
    }

    //Mixing Recipes
    public static Map<Identifier, MixingRecipe> cachedMixingRecipes = new HashMap<>();

    public static void setCachedMixingRecipes(Map<Identifier, MixingRecipe> cachedMixingRecipes) {
        ClientRecipeCache.cachedMixingRecipes = cachedMixingRecipes;
    }

    public static Collection<MixingRecipe> getCachedMixingRecipes() {
        return cachedMixingRecipes.values();
    }

    //Fuel Recipes
    public static Map<Identifier, FuelRecipe> cachedFuelRecipes = new HashMap<>();

    public static void setCachedFuelRecipes(Map<Identifier, FuelRecipe> cachedFuelRecipes) {
        ClientRecipeCache.cachedFuelRecipes = cachedFuelRecipes;
    }

    public static Collection<FuelRecipe> getCachedFuelRecipes() {
        return cachedFuelRecipes.values();
    }






}
