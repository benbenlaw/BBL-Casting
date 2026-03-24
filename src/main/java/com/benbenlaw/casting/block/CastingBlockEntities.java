package com.benbenlaw.casting.block;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.entity.ControllerBlockEntity;
import com.benbenlaw.casting.block.entity.MixerBlockEntity;
import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.casting.block.entity.TankBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class CastingBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Casting.MOD_ID);

    public static final Supplier<BlockEntityType<ControllerBlockEntity>> CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("controller_block_entity", () ->
                    new BlockEntityType<>(ControllerBlockEntity::new, CastingBlocks.CONTROLLER.get()));

    public static final Supplier<BlockEntityType<SolidifierBlockEntity>> SOLIDIFIER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("solidifier_block_entity", () ->
                    new BlockEntityType<>(SolidifierBlockEntity::new, CastingBlocks.SOLIDIFIER.get()));

    public static final Supplier<BlockEntityType<MixerBlockEntity>> MIXER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mixer_block_entity", () ->
                    new BlockEntityType<>(MixerBlockEntity::new, CastingBlocks.MIXER.get()));

    public static final Supplier<BlockEntityType<TankBlockEntity>> FUEL_TANK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fuel_tank_block_entity", () ->
                    new BlockEntityType<>(TankBlockEntity::new, CastingBlocks.FUEL_TANK.get()));

    public static final Supplier<BlockEntityType<TankBlockEntity>> COOLANT_TANK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("coolant_tank_block_entity", () ->
                    new BlockEntityType<>(TankBlockEntity::new, CastingBlocks.COOLANT_TANK.get()));

}
