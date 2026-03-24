package com.benbenlaw.casting.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Map;

public class FilteredItemHandler extends ItemStackHandler {

    public final Map<Integer, Item> allowedItems;


    public FilteredItemHandler(int size, Map<Integer, Item> allowedItems) {
        super(size);
        this.allowedItems = allowedItems;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        Item allowed = allowedItems.get(slot);
        if (allowed == null) {
            return true;
        }
        return stack.getItem() == allowed;
    }


}
