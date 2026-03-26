package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;import com.benbenlaw.casting.recipe.custom.MixingRecipe;
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
import mezz.jei.api.recipe.types.IRecipeType;import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MixingRecipeCategory implements IRecipeCategory<MixingRecipe> {

    public final static Identifier TEXTURE = Casting.identifier("textures/gui/mixing_jei.png");
    public static final IRecipeType<MixingRecipe> RECIPE_TYPE = IRecipeType.create(Casting.identifier("mixing"), MixingRecipe.class);

    private final int width = 101;
    private final int height = 20;
    private final IDrawable icon;

    @Override
    public @Nullable Identifier getIdentifier(MixingRecipe recipe) {
        return ClientRecipeCache.getCachedMixingRecipes().stream()
                .filter(r -> r.equals(recipe))
                .findFirst()
                .map(r -> {
                    // Find the corresponding ID in the cache map
                    for (Map.Entry<Identifier, MixingRecipe> entry : ClientRecipeCache.cachedMixingRecipes.entrySet()) {
                        if (entry.getValue().equals(recipe)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    public MixingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingBlocks.MIXER.get()));
    }


    @Override
    public @NotNull IRecipeType<MixingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.casting.mixing");
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
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, MixingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        int centerX = 38;
        int centerY = 2;
        int slotWidth = 18;

        List<SizedFluidIngredient> fluids = recipe.fluids();
        int totalFluids = fluids.size();

        for (int i = 0; i < totalFluids; i++) {
            int displayIndex = Math.min(i, 2);
            int xPos = centerX - (displayIndex * slotWidth);

            final int finalIndex = i;

            builder.addSlot(RecipeIngredientRole.INPUT, xPos, centerY)
                    .add(fluids.get(i).ingredient().fluids().getFirst().value(), fluids.get(i).amount())
                    .addRichTooltipCallback((slot, tooltip) ->
                            tooltip.add(Component.literal(fluids.get(finalIndex).amount() + " mB")
                                    .withStyle(ChatFormatting.GOLD)))
                    .setBackground(JEICastingPlugin.slotDrawable, -1, -1);
        }

        // Output Slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 2)
                .add(recipe.outputFluid().fluid().value(), recipe.outputFluid().amount())
                .addRichTooltipCallback((slot, tooltip) ->
                        tooltip.add(Component.literal(recipe.outputFluid().amount() + " mB")
                                .withStyle(ChatFormatting.GOLD)))
                .setBackground(JEICastingPlugin.slotDrawable, -1, -1);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MixingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 54, 1, 0, 0, 28, 18)) {
            tooltip.add(Component.translatable("tooltip.core.ticks", 200));
        }
    }

    @Override
    public void draw(MixingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
    }

    public void createRecipeExtras(IRecipeExtrasBuilder builder, MixingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> inputFluids = recipeSlots.getSlots(RecipeIngredientRole.INPUT);

        if (inputFluids.size() > 3) {
            IScrollGridWidget triggersGrid = builder.addScrollGridWidget(inputFluids, 2, 1);
            triggersGrid.setPosition(1, 1);
        }
        builder.addAnimatedRecipeArrow(200).setPosition(57, 2);
    }
}