package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
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
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.List;

public class MixerScreen extends AbstractContainerScreen<MixerMenu> {

    private static final Identifier TEXTURE = Casting.identifier("textures/gui/mixer_gui.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    public MixerScreen(MixerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.isCrafting()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0, x + 117, y + 34, menu.getScaledProgress() + 1, 16);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderTankTextures(guiGraphics, x, y, mouseX, mouseY);
        DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));
        renderTankTooltips(guiGraphics, x, y, mouseX, mouseY);
    }

    private void renderTankTextures(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 20, 16, 47);
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 1, x + 35, y + 20, 16, 47);
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 2, x + 62, y + 20, 16, 47);
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 3, x + 89, y + 20, 16, 47);

        drawTankFluid(guiGraphics, menu.blockEntity.getOutputFluidHandler(), 0, x + 152, y + 20, 16, 47);
    }

    private void drawTankFluid(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height) {
        var fluidHandler = (net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

        if (!stack.isEmpty()) {
            int capacity = fluidHandler.getCapacityAsInt(slot, FluidResource.of(stack));
            int displayLevel = (int)((float)stack.getAmount() / (float)capacity * (float)height);

            FluidRenderingUtils.renderFluidStack(guiGraphics, stack, x, y + height - displayLevel, width, displayLevel, 0, 0);
        }
    }

    private void renderTankTooltips(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 20, 16, 47, mouseX, mouseY);
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 1, x + 35, y + 20, 16, 47, mouseX, mouseY);
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 2, x + 62, y + 20, 16, 47, mouseX, mouseY);
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 3, x + 89, y + 20, 16, 47, mouseX, mouseY);

        drawTankTooltip(guiGraphics, menu.blockEntity.getOutputFluidHandler(), 0, x + 152, y + 20, 16, 47, mouseX, mouseY);
    }

    private void drawTankTooltip(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height, int mouseX, int mouseY) {
        var fluidHandler = (FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

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
                FluidRenderingUtils.renderFluidStackTooltip(guiGraphics, stack, fluidHandler, slot, x, y, width, height, mouseX, mouseY);
            }
        }
    }
}