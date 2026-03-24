package com.benbenlaw.casting.event.client;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.core.util.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

@EventBusSubscriber(modid = Casting.MOD_ID)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.has(CastingDataComponents.FLUIDS.get())) {
            FluidListComponent fluidListComponent = stack.get(CastingDataComponents.FLUIDS.get());

            if (fluidListComponent == null || fluidListComponent.fluids().isEmpty()) return;

            if (Minecraft.getInstance().hasShiftDown()) {
                event.getToolTip().add(Component.translatable("tooltip.casting.fluids_header")
                        .withStyle(ChatFormatting.BLUE));

                for (FluidStack fluid : fluidListComponent.fluids()) {
                    if (!fluid.isEmpty()) {
                        Component fluidLine = Component.literal(" - ")
                                .append(Component.literal(fluid.getAmount() + "mB "))
                                .append(fluid.getHoverName())
                                .withStyle(ChatFormatting.BLUE);

                        event.getToolTip().add(fluidLine);
                    }
                }
            } else {
                event.getToolTip().add(Component.translatable("tooltip.bblcore.shift")
                        .withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}
