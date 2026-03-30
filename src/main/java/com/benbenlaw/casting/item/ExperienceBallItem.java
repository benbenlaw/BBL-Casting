package com.benbenlaw.casting.item;

import com.benbenlaw.casting.config.CastingConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class ExperienceBallItem extends Item {
    public ExperienceBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (!level.isClientSide()) {
            player.giveExperiencePoints(CastingConfig.amountOfExperienceFromExperienceBall.get());
            player.getItemInHand(hand).shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
