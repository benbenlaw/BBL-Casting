package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;
import com.benbenlaw.casting.recipe.*;
import com.benbenlaw.casting.screen.ControllerScreen;
import com.benbenlaw.casting.screen.MixerScreen;
import com.benbenlaw.casting.screen.SolidifierScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;


@JeiPlugin
public class JEICastingPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    @Override
    public @NotNull Identifier getPluginUid() {
        return Casting.identifier("jei_plugin");
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAlias(CastingBlocks.CONTROLLER.toStack(), "Smeltery Controller");
        registration.addAlias(CastingBlocks.SOLIDIFIER.toStack(), "Casting Table)");
        registration.addAlias(CastingBlocks.MIXER.toStack(), "Alloyer");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MeltingRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.CONTROLLER));
        registration.addCraftingStation(SolidifierRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.SOLIDIFIER));
        registration.addCraftingStation(MixingRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.MIXER));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();

        registration.addRecipeCategories(new MeltingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SolidifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MixingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MeltingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedMeltingRecipes().stream().toList());
        registration.addRecipes(SolidifierRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedSolidifierRecipes().stream().toList());
        registration.addRecipes(MixingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedMixingRecipes().stream().toList());
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ControllerScreen.class, 117, 34, 24, 16, MeltingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(MixerScreen.class, 117, 34, 24, 16, MixingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SolidifierScreen.class, 76, 34, 24, 16, SolidifierRecipeCategory.RECIPE_TYPE);

    }

}


