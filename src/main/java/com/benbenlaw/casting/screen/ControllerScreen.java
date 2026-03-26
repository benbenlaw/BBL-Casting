package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.entity.ControllerBlockEntity;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import com.benbenlaw.core.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.List;
import java.util.Objects;

public class ControllerScreen extends AbstractContainerScreen<ControllerMenu> {

    private static final Identifier TEXTURE = Casting.identifier("textures/gui/controller_gui.png");
    private static final Identifier PROGRESS_ARROW = Casting.identifier("controller_progress");

    public ControllerScreen(ControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.isCrafting()) {
            for (int i = 0; i < 15; i++) {
                int scaledHeight = menu.getScaledProgress(i);

                if (scaledHeight > 0) {
                    int row = i / 5;
                    int col = i % 5;
                    int slotX = x + 8 + (col * 19);
                    int slotY = y + 16 + (row * 19);
                    int yOffset = 16 - scaledHeight;

                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, slotX, slotY + yOffset, 16, scaledHeight);

                    int xPos = 8 + (i * 10);
                    if (i >= 5) xPos += 5;
                    if (i >= 10) xPos += 5;

                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Core.identifier("duration_icon"), x + xPos, y - 5, 10, 10);
                }
            }
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderTankTextures(guiGraphics, x, y, mouseX, mouseY);

        renderDurationTooltips(guiGraphics, x, y, mouseX, mouseY);
        renderTankTooltips(guiGraphics, x, y, mouseX, mouseY);
        if (ControllerBlockEntity.getActiveFuelTank(menu.level, menu.blockPos) != null) {
            FluidRenderingUtils.renderFluid(guiGraphics,
                    Objects.requireNonNull(ControllerBlockEntity.getActiveFuelTank(menu.level, menu.blockPos)).getInputFluidHandler(), 0, x, y, 110, 51, 16, 16, mouseX, mouseY);
        } else if (MouseUtil.isMouseOver(mouseX, mouseY, x + 110, y + 51, 16, 16)) {
            Component text = Component.translatable("tooltip.casting.no_fuel");
            List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
            guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }


    private void renderDurationTooltips(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        for (int i = 0; i < 15; i++) {
            if (menu.data.get(i) > 0) {
                int xPos = 8 + (i * 10);
                if (i >= 5) xPos += 5;
                if (i >= 10) xPos += 5;

                if (mouseX >= x + xPos && mouseX < x + xPos + 10 && mouseY >= y - 5 && mouseY < y + 5) {
                    Component text = Component.translatable("tooltip.core.duration_tooltip", menu.data.get(i), menu.data.get(i + 15));
                    guiGraphics.tooltip(this.font, List.of(ClientTooltipComponent.create(text.getVisualOrderText())), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
                }
            }
        }
    }

    private void renderTankTextures(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {

        drawTankFluid(guiGraphics, 0, x + 134, y + 18, 16, 23);
        drawTankFluid(guiGraphics, 1, x + 152, y + 18, 16, 23);
        drawTankFluid(guiGraphics, 2, x + 134, y + 45, 16, 23);
        drawTankFluid(guiGraphics, 3, x + 152, y + 45, 16, 23);
    }

    private void drawTankFluid(GuiGraphicsExtractor guiGraphics, int slot, int x, int y, int width, int height) {
        var stack = FluidUtil.getStack(menu.blockEntity.getOutputFluidHandler(), slot);
        if (!stack.isEmpty()) {

            int capacity = menu.blockEntity.getOutputFluidHandler().getCapacityAsInt(slot, net.neoforged.neoforge.transfer.fluid.FluidResource.of(stack));
            int displayLevel = (int)((float)stack.getAmount() / (float)capacity * (float)height);

            FluidRenderingUtils.renderFluidStack(guiGraphics, stack, x, y + height - displayLevel, width, displayLevel, 0, 0);
        }
    }

    private void renderTankTooltips(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        drawTankTooltip(guiGraphics, 0, x + 134, y + 18, 16, 23, mouseX, mouseY);
        drawTankTooltip(guiGraphics, 1, x + 152, y + 18, 16, 23, mouseX, mouseY);
        drawTankTooltip(guiGraphics, 2, x + 134, y + 45, 16, 23, mouseX, mouseY);
        drawTankTooltip(guiGraphics, 3, x + 152, y + 45, 16, 23, mouseX, mouseY);
    }

    private void drawTankTooltip(GuiGraphicsExtractor guiGraphics, int slot, int x, int y, int width, int height, int mouseX, int mouseY) {
        var stack = FluidUtil.getStack(menu.blockEntity.getOutputFluidHandler(), slot);

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {

            if (stack.isEmpty()) {
                Component text = Component.literal("Empty");
                List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
                guiGraphics.tooltip(
                        this.font,
                        components,
                        mouseX,
                        mouseY,
                        DefaultTooltipPositioner.INSTANCE,
                        null
                );
            } else {
                FluidRenderingUtils.renderFluidStackTooltip(guiGraphics, stack, menu.blockEntity.getOutputFluidHandler(), slot, x, y, width, height, mouseX, mouseY);
            }
        }
    }
}
