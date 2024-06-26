package com.weever.rotp_mih.network.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AddTagPacket {
    private final int entityId;
    private final String tag;

    public AddTagPacket(int entityId, String tag) {
        this.entityId = entityId;
        this.tag = tag;
    }

    public static void encode(AddTagPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeUtf(msg.tag);
    }

    public static AddTagPacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        String tag = buf.readUtf();
        return new AddTagPacket(entityId, tag);
    }

    public static void handle(AddTagPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = com.github.standobyte.jojo.client.ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.addTag(msg.tag);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}