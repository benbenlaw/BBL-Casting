package com.benbenlaw.casting.block;

import com.benbenlaw.casting.block.entity.ControllerBlockEntity;
import com.benbenlaw.casting.block.entity.MixerBlockEntity;
import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.casting.block.entity.TankBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CastingCapabilities {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        //Controller
        event.registerBlockEntity(Capabilities.Item.BLOCK, CastingBlockEntities.CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemCapability());

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CastingBlockEntities.CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getFluidCapability());


        //Solidifier
        event.registerBlockEntity(Capabilities.Item.BLOCK, CastingBlockEntities.SOLIDIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemCapability());

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CastingBlockEntities.SOLIDIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getFluidCapability());


        //Mixer
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CastingBlockEntities.MIXER_BLOCK_ENTITY
                .get(), (blockEntity, side) -> blockEntity.getFluidCapability());


    }
}
