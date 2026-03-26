package com.benbenlaw.casting.recipe;

import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record MeltingRecipeInput(InputItemHandler handler, int temp) implements RecipeInput {

    @Override
    public @NonNull ItemStack getItem(int i) {
        return handler.getResource(i).toStack();
    }

    @Override
    public int size() {
        return handler.size();
    }
}
