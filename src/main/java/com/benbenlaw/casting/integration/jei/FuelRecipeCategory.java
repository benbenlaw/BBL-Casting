package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;
import com.benbenlaw.casting.recipe.custom.FuelRecipe;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import com.benbenlaw.core.util.MouseUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawablesView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FuelRecipeCategory implements IRecipeCategory<FuelRecipe> {

    public final static Identifier TEXTURE = Casting.identifier("textures/gui/fuel_jei.png");

    public static final IRecipeType<FuelRecipe> RECIPE_TYPE = IRecipeType.create(Casting.identifier("fuel"), FuelRecipe.class);

    private final int width = 104;
    private final int height = 56;
    private final IDrawable icon;
    private int tabsUsed = 0;

    @Override
    public boolean isHandled(FuelRecipe recipe) {
        return tabsUsed == 0;
    }

    @Override
    public @Nullable Identifier getIdentifier(FuelRecipe recipe) {
        return ClientRecipeCache.getCachedFuelRecipes().stream()
                .filter(r -> r.equals(recipe))
                .findFirst()
                .map(r -> {
                    // Find the corresponding ID in the cache map
                    for (Map.Entry<Identifier, FuelRecipe> entry : ClientRecipeCache.cachedFuelRecipes.entrySet()) {
                        if (entry.getValue().equals(recipe)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    public FuelRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingBlocks.TANK.get()));
    }

    @Override
    public @NotNull IRecipeType<FuelRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.casting.fuel");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FuelRecipe recipe, IFocusGroup focusGroup) {
        tabsUsed++;

        List<FuelRecipe> recipes = ClientRecipeCache.getCachedFuelRecipes().stream().toList();
        List<FuelRecipe> mutableRecipes = new ArrayList<>(recipes);

        mutableRecipes.sort(Comparator.comparingInt(FuelRecipe::temp).reversed());

        for (int i = 0; i < mutableRecipes.size(); i++) {

            FuelRecipe fuel = mutableRecipes.get(i);

            builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).add(fuel.fluid().ingredient().display())
                    .addRichTooltipCallback((slot, tooltip) ->
                            tooltip.add(Component.literal(fuel.fluid().amount() + "mB").withStyle(ChatFormatting.GOLD)))
                    .addRichTooltipCallback((slot, tooltip) ->
                            tooltip.add(Component.translatable("jei.casting.fuel_temp", fuel.temp())
                                    .withStyle(fuel.temp() >= 1000 ? ChatFormatting.RED : ChatFormatting.AQUA)));
        }

    }

    @Override
    public void draw(FuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
    }

    public void createRecipeExtras(IRecipeExtrasBuilder builder, FuelRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> inputFluids = recipeSlots.getSlots(RecipeIngredientRole.INPUT);

        IScrollGridWidget triggersGrid = builder.addScrollGridWidget(inputFluids, 5, 3);
        triggersGrid.setPosition(0, 1);

    }

}