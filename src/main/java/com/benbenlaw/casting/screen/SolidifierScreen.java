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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

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
        renderTankTextures(guiGraphics, x, y);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));
        renderTankTooltips(guiGraphics, x, y, mouseX, mouseY);
    }

    private void renderTankTextures(GuiGraphicsExtractor guiGraphics, int x, int y) {
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 44, 16, 23);

        drawTankFluid(guiGraphics, menu.blockEntity.getFilterFluidHandler(), 0, x + 8, y + 20, 16, 16);

        var fuelTank = SolidifierBlockEntity.getActiveFuelTank(menu.level, menu.blockPos);
        if (fuelTank != null) {
            drawTankFluid(guiGraphics, fuelTank.getInputFluidHandler(), 0, x + 152, y + 51, 16, 16);
        }
    }

    private void drawTankFluid(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height) {
        var fluidHandler = (net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

        if (!stack.isEmpty()) {
            int capacity = fluidHandler.getCapacityAsInt(slot, FluidResource.of(stack));
            int displayLevel = (int) ((float) stack.getAmount() / (float) capacity * (float) height);
            FluidRenderingUtils.renderFluidStack(guiGraphics, stack, x, y + height - displayLevel, width, displayLevel, 0, 0);
        }
    }

    private void renderTankTooltips(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 44, 16, 23, mouseX, mouseY, "Empty");
        drawTankTooltip(guiGraphics, menu.blockEntity.getFilterFluidHandler(), 0, x + 8, y + 20, 16, 16, mouseX, mouseY, "Empty Filter");

        var fuelTank = SolidifierBlockEntity.getActiveFuelTank(menu.level, menu.blockPos);
        if (fuelTank != null) {
            drawTankTooltip(guiGraphics, fuelTank.getInputFluidHandler(), 0, x + 152, y + 51, 16, 16, mouseX, mouseY, "Empty");
        } else if (MouseUtil.isMouseOver(mouseX, mouseY, x + 152, y + 51, 16, 16)) {
            Component text = Component.translatable("tooltip.casting.no_coolant");
            List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
            guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    private void drawTankTooltip(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height, int mouseX, int mouseY, String emptyName) {
        var fluidHandler = (FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            if (stack.isEmpty()) {
                Component text = Component.literal(emptyName);
                List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
                guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            } else {
                FluidRenderingUtils.renderFluidStackTooltip(guiGraphics, stack, fluidHandler, slot, x, y, width, height, mouseX, mouseY);
            }
        }
    }
}