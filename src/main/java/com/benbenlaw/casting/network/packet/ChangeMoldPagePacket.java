package com.benbenlaw.casting.network.packet;


import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.screen.SolidifierMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record ChangeMoldPagePacket(BlockPos pos, int page) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ChangeMoldPagePacket> TYPE = new CustomPacketPayload.Type<>(Casting.identifier("change_mold_page"));

    public static final IPayloadHandler<ChangeMoldPagePacket> HANDLER = (packet, context) -> {
        context.enqueueWork(() -> {

            if (context.player().containerMenu instanceof SolidifierMenu menu) {
                menu.setMoldPage(packet.page());
            }

        });
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeMoldPagePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ChangeMoldPagePacket::pos,
            ByteBufCodecs.INT, ChangeMoldPagePacket::page,
            ChangeMoldPagePacket::new
    );

    public CustomPacketPayload.Type<ChangeMoldPagePacket> type() {
        return TYPE;
    }



}
