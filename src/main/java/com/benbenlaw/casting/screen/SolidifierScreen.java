package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.entity.ControllerBlockEntity;
import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import com.benbenlaw.core.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Objects;

public class SolidifierScreen extends AbstractContainerScreen<SolidifierMenu> {

    private static final Identifier TEXTURE = Casting.identifier("textures/gui/solidifier_gui.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    public SolidifierScreen(SolidifierMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.isCrafting()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0, x + 76, y + 34, menu.getScaledProgress() + 1, 16);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;


        DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));
        renderTanks(guiGraphics, x, y, mouseX, mouseY);

        if (SolidifierBlockEntity.getActiveFuelTank(menu.level, menu.blockPos) != null) {
            FluidRenderingUtils.renderFluid(guiGraphics,
                    Objects.requireNonNull(SolidifierBlockEntity.getActiveFuelTank(menu.level, menu.blockPos)).getInputFluidHandler(), 0, x, y, 152, 51, 16, 16, mouseX, mouseY);
        } else if (MouseUtil.isMouseOver(mouseX, mouseY, x + 152, y + 51, 16, 16)) {
            Component text = Component.translatable("tooltip.casting.no_coolant");
            List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
            guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    private void renderTanks(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        FluidRenderingUtils.renderFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x, y, 8, 20, 47, 16, mouseX, mouseY);
    }
}