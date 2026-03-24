package com.benbenlaw.casting.event;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.event.client.ClientRecipeCache;
import com.benbenlaw.casting.recipe.CastingRecipes;
import com.benbenlaw.casting.recipe.MeltingRecipe;
import com.benbenlaw.casting.recipe.MixingRecipe;
import com.benbenlaw.casting.recipe.SolidifierRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Casting.MOD_ID)
public class RecipeEvents {

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(CastingRecipes.MELTING_TYPE.get());
        event.sendRecipes(CastingRecipes.SOLIDIFIER_TYPE.get());
        event.sendRecipes(CastingRecipes.MIXING_TYPE.get());
    }

    @SubscribeEvent
    public static void onRecipeReceived(RecipesReceivedEvent event) {
        RecipeMap recipeMap = event.getRecipeMap();

        //Melting
        Collection<RecipeHolder<MeltingRecipe>> meltingRecipe = recipeMap.byType(CastingRecipes.MELTING_TYPE.get());
        Map<Identifier, MeltingRecipe> meltingRecipeMap = new HashMap<>();

        for (RecipeHolder<MeltingRecipe> recipeHolder : meltingRecipe) {
            meltingRecipeMap.put(recipeHolder.id().identifier(), recipeHolder.value());
        }
        ClientRecipeCache.setCachedMeltingRecipes(meltingRecipeMap);

        //Solidifier
        Collection<RecipeHolder<SolidifierRecipe>> solidifierRecipe = recipeMap.byType(CastingRecipes.SOLIDIFIER_TYPE.get());
        Map<Identifier, SolidifierRecipe> solidifierRecipeMap = new HashMap<>();

        for (RecipeHolder<SolidifierRecipe> recipeHolder : solidifierRecipe) {
            solidifierRecipeMap.put(recipeHolder.id().identifier(), recipeHolder.value());
        }
        ClientRecipeCache.setCachedSolidifierRecipes(solidifierRecipeMap);

        //Mixing
        Collection<RecipeHolder<MixingRecipe>> mixingRecipe = recipeMap.byType(CastingRecipes.MIXING_TYPE.get());
        Map<Identifier, MixingRecipe> mixingRecipeMap = new HashMap<>();

        for (RecipeHolder<MixingRecipe> recipeHolder : mixingRecipe) {
            mixingRecipeMap.put(recipeHolder.id().identifier(), recipeHolder.value());
        }
        ClientRecipeCache.setCachedMixingRecipes(mixingRecipeMap);

    }

}
