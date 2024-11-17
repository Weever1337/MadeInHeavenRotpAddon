package com.weever.rotp_mih.network.fromclient;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncClientWorldCapPacket {
    public final int entityId;

    public SyncClientWorldCapPacket(int entityId) {
        this.entityId = entityId;
    }

    public static class Handler implements IModPacketHandler<SyncClientWorldCapPacket> {
        @Override
        public void encode(SyncClientWorldCapPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
        }

        @Override
        public SyncClientWorldCapPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new SyncClientWorldCapPacket(entityId);
        }

        @Override
        public void handle(SyncClientWorldCapPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ctx.get().getSender().getLevel().getEntity(msg.entityId);
            if (entity instanceof PlayerEntity) {
                WorldCapProvider.getWorldCap((ServerWorld) entity.level).syncData((PlayerEntity) entity);
            }
        }

        @Override
        public Class<SyncClientWorldCapPacket> getPacketClass() {
            return SyncClientWorldCapPacket.class;
        }
    }
}
