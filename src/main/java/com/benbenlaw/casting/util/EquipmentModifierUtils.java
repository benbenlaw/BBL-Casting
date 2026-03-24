package com.benbenlaw.casting.util;
/*
import com.benbenlaw.casting.config.EquipmentModifierConfig;
import com.benbenlaw.casting.item.EquipmentModifier;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import static com.benbenlaw.casting.item.EquipmentModifier.*;

public class EquipmentModifierUtils {

    public static boolean isEffectAtMax(ItemStack stack, EquipmentModifier modifier) {

        if (modifier == SILK_TOUCH) {
            int fortuneLevel = (int) stack.getOrDefault(FORTUNE.dataComponent.get(), 0);
            if (fortuneLevel > 0) {
                return false;
            }
        }

        if (modifier == FORTUNE) {
            boolean hasSilkTouch = (boolean) stack.getOrDefault(SILK_TOUCH.dataComponent.get(), false);
            if (hasSilkTouch) {
                return false;
            }
        }

        if (stack.getComponents().has(modifier.dataComponent.get())) {
            if (modifier.codec == Codec.BOOL) {
                return (boolean) stack.getOrDefault(modifier.dataComponent.get(), false);
            } else if (modifier.codec == Codec.INT) {
                int currentLevel = (int) stack.getOrDefault(modifier.dataComponent.get(), 0);
                return currentLevel >= modifier.maxLevel.get();
            } else {
                throw new IllegalArgumentException("Unsupported codec type for modifier: " + modifier.id);
            }
        }

        return false;
    }


    @SuppressWarnings("unchecked")
    public static ItemStack copyAndApplyEffect(ItemStack stack, EquipmentModifier modifier) {
        ItemStack copy = stack.copy();
        DataComponentType<?> component = modifier.dataComponent.get();

        if (modifier.codec == Codec.BOOL) {
            boolean currentValue = (boolean) stack.getOrDefault(component, false);
            copy.set((DataComponentType<Boolean>) component, !currentValue); // toggle or set true
        } else if (modifier.codec == Codec.INT) {
            int currentLevel = (int) stack.getOrDefault(component, 0);
            int newLevel = Math.min(currentLevel + 1, modifier.maxLevel.get());
            copy.set((DataComponentType<Integer>) component, newLevel); // increment level
        } else {
            throw new IllegalArgumentException("Unsupported codec type for modifier: " + modifier.id);
        }

        return copy;
    }

    @SuppressWarnings("inverted")
    public static boolean hasEnoughFreeModifiers(ItemStack stack, EquipmentModifier modifier) {

        if (!stack.has(EQUIPMENT_LEVEL.dataComponent.get())) {
            return true;
        }
        if (stack.has(modifier.dataComponent.get())) {
            return true;
        }
        int toolLevel = (int) stack.getOrDefault(EQUIPMENT_LEVEL.dataComponent.get(), 0);
        int modifierCount = 0;

        for (EquipmentModifier modifiers : EquipmentModifier.values()) {
            if (modifiers == EQUIPMENT_LEVEL) {
                continue;
            }
            if (stack.has(modifiers.dataComponent.get())) {
                modifierCount++;
            }
        }
        return toolLevel > modifierCount;
    }

    public static double getExperienceModifierLevel(int toolLevel) {
        return toolLevel * EquipmentModifierConfig.experienceMultiplierPerLevel.get();

    }
}

 */
