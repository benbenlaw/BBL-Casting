package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import com.benbenlaw.core.util.MouseUtil;import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SolidifierRecipeCategory implements IRecipeCategory<SolidifierRecipe> {

    public final static Identifier TEXTURE = Casting.identifier("textures/gui/solidifier_jei.png");

    public static final IRecipeType<SolidifierRecipe> RECIPE_TYPE = IRecipeType.create(Casting.identifier("solidifier"), SolidifierRecipe.class);

    private final int width = 101;
    private final int height = 20;
    private final IDrawable icon;

    @Override
    public @Nullable Identifier getIdentifier(SolidifierRecipe recipe) {
        return ClientRecipeCache.getCachedSolidifierRecipes().stream()
                .filter(r -> r.equals(recipe))
                .findFirst()
                .map(r -> {
                    // Find the corresponding ID in the cache map
                    for (Map.Entry<Identifier, SolidifierRecipe> entry : ClientRecipeCache.cachedSolidifierRecipes.entrySet()) {
                        if (entry.getValue().equals(recipe)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    public SolidifierRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingBlocks.SOLIDIFIER.get()));
    }

    @Override
    public @NotNull IRecipeType<SolidifierRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.casting.solidifier");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SolidifierRecipe recipe, IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).add(recipe.mold().ingredient());

        builder.addSlot(RecipeIngredientRole.INPUT, 38, 2).add(recipe.fluid().fluid().value(), recipe.fluid().amount())
                .addRichTooltipCallback((slot, tooltip) ->
                        tooltip.add(Component.literal(recipe.fluid().amount() + " mB").withStyle(ChatFormatting.GOLD)));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 2).add(recipe.output().ingredient());
    }

    @Override
    public void draw(SolidifierRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);

    }

    public void createRecipeExtras(IRecipeExtrasBuilder builder, SolidifierRecipe recipe, IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(200).setPosition(57, 2);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, SolidifierRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 56, 10, 0, 0, 28, 18)) {
            double modifier = recipe.durationModifier().orElse(1.0);
            String timeString = String.valueOf((200 * modifier)).replace(".0", "");
            tooltip.add(Component.translatable("tooltip.core.ticks", timeString));
        }
    }
}