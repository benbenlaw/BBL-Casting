package com.benbenlaw.casting.integration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;import com.benbenlaw.casting.recipe.custom.MeltingRecipe;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MeltingRecipeCategory implements IRecipeCategory<MeltingRecipe> {

    public final static Identifier TEXTURE = Casting.identifier("textures/gui/melting_jei.png");
    public static final IRecipeType<MeltingRecipe> RECIPE_TYPE = IRecipeType.create(Casting.identifier("melting"), MeltingRecipe.class);

    private final int width = 101;
    private final int height = 20;
    private final IDrawable icon;

    @Override
    public @Nullable Identifier getIdentifier(MeltingRecipe recipe) {
        return ClientRecipeCache.getCachedMeltingRecipes().stream()
                .filter(r -> r.equals(recipe))
                .findFirst()
                .map(r -> {
                    // Find the corresponding ID in the cache map
                    for (Map.Entry<Identifier, MeltingRecipe> entry : ClientRecipeCache.cachedMeltingRecipes.entrySet()) {
                        if (entry.getValue().equals(recipe)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }
    public MeltingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingBlocks.CONTROLLER.get()));
    }

    @Override
    public @NotNull IRecipeType<MeltingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.casting.melting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MeltingRecipe recipe, IFocusGroup focusGroup) {
        int centerX = 48;
        int centerY = 2;
        int slotWidth = 18;

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 2).add(recipe.input().ingredient())
                .setBackground(JEICastingPlugin.slotDrawable, -1, -1);

        List<FluidStackTemplate> fluids = recipe.output();
        int totalFluids = fluids.size();

        for (int i = 0; i < totalFluids; i++) {
            int displayIndex = Math.min(i, 2);
            int xPos = centerX + (displayIndex * slotWidth);

            final int finalIndex = i;

            builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, centerY)
                    .add(fluids.get(i).fluid().value(), fluids.get(i).amount())
                    .addRichTooltipCallback((slot, tooltip) -> {
                        tooltip.add(Component.literal(fluids.get(finalIndex).amount() + "mB").withStyle(ChatFormatting.GOLD));
                        tooltip.add(Component.translatable("gui.casting.jei.melting_temp", recipe.meltingTemp()).withStyle(ChatFormatting.GOLD));
                    })
                    .setBackground(JEICastingPlugin.slotDrawable, -1, -1);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 19, 1, 0, 0, 28, 18)) {
            double modifier = recipe.durationModifier().orElse(1.0);
            String timeString = String.valueOf((200 * modifier)).replace(".0", "");
            tooltip.add(Component.translatable("tooltip.core.ticks", timeString));
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, MeltingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> outputFluids = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT);

        if (outputFluids.size() > 3) {
            IScrollGridWidget triggersGrid = builder.addScrollGridWidget(outputFluids, 2, 1);
            triggersGrid.setPosition(47, 1);
        }
        builder.addAnimatedRecipeArrow(200).setPosition(21, 2);
    }

    @Override
    public void draw(MeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
    }

}