package com.benbenlaw.casting.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.benbenlaw.casting.item.EquipmentModifier.UNBREAKING;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void casting$customDurabilityHandling(
            int amount,
            ServerLevel level,
            LivingEntity entity,
            Consumer<Item> onBreak,
            CallbackInfo ci
    ) {
        ItemStack self = (ItemStack)(Object)this;

        if (!self.isDamageableItem()) return;

        if (self.getComponents().has(UNBREAKING.dataComponent.get())) {

            int lvl = (int) self.getOrDefault(UNBREAKING.dataComponent.get(), 0);
            float chance = lvl * 0.1f;

            RandomSource random = entity.getRandom();

            if (random.nextFloat() < chance) {
                ci.cancel(); // fully prevents durability loss
            }
        }
    }
}