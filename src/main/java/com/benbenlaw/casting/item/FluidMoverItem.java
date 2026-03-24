package com.benbenlaw.casting.item;

import net.minecraft.world.item.Item;

public class FluidMoverItem extends Item {
    public FluidMoverItem(Properties properties) {
        super(properties);
    }


    /*
    @Override
    public void appendHoverText(ItemStack itemStack, @NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            FluidListComponent component = itemStack.get(CastingDataComponents.FLUIDS);
            if (component != null) {
                List<FluidStack> fluids = component.fluids();
                if (!fluids.isEmpty()) {
                    FluidType fluid = fluids.get(0).getFluidType();
                    int fluidAmount = fluids.get(0).getAmount();
                    components.add(Component.literal("Fluids: ").withStyle(ChatFormatting.BLUE));
                    components.add(Component.literal("- ").append(fluidAmount + "mb ").append(Component.translatable(fluid.getDescriptionId())).withStyle(ChatFormatting.GREEN));
                }
            }
        } else {
            components.add(Component.translatable("tooltips.bblcore.shift").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(itemStack, context, components, flag);
    }

     */

}
