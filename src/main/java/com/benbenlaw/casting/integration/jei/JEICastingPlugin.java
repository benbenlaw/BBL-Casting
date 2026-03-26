package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.CastingProcessingRecipeProvider;
import com.benbenlaw.casting.event.client.ClientRecipeCache;
import com.benbenlaw.casting.recipe.*;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


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
        registration.addAlias(CastingBlocks.TANK.toStack(), "Tank");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MeltingRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.CONTROLLER));
        registration.addCraftingStation(SolidifierRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.SOLIDIFIER));
        registration.addCraftingStation(MixingRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.MIXER));
        registration.addCraftingStation(FuelRecipeCategory.RECIPE_TYPE, new ItemStack(CastingBlocks.TANK));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();

        registration.addRecipeCategories(new MeltingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SolidifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MixingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FuelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MeltingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedMeltingRecipes().stream().toList());

        List<SolidifierRecipe> solidifierRecipes = new java.util.ArrayList<>(ClientRecipeCache.getCachedSolidifierRecipes().stream().toList());

        for (Fluid fluid : BuiltInRegistries.FLUID) {
            if (fluid.isSource(fluid.defaultFluidState())) {

                Item bucketItem = fluid.getBucket();
                if (bucketItem != Items.AIR && bucketItem != Items.BUCKET) {

                    SolidifierRecipe bucketRecipe = new SolidifierRecipe(
                            SizedIngredient.of(Items.BUCKET, 1),
                            new SizedIngredient(Ingredient.of(bucketItem), 1),
                            new FluidStackTemplate(fluid, 1000),
                            1000,
                            Optional.of(0.1)
                    );

                    solidifierRecipes.add(bucketRecipe);
                }
            }
        }

        registration.addRecipes(SolidifierRecipeCategory.RECIPE_TYPE, solidifierRecipes);
        registration.addRecipes(MixingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedMixingRecipes().stream().toList());
        registration.addRecipes(FuelRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedFuelRecipes().stream().toList());
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ControllerScreen.class, 106, 34, 24, 16, MeltingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(MixerScreen.class, 117, 34, 24, 16, MixingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SolidifierScreen.class, 76, 34, 24, 16, SolidifierRecipeCategory.RECIPE_TYPE);

    }

}


