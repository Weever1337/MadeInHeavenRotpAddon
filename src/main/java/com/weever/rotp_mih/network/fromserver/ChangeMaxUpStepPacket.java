package com.weever.rotp_mih.network.fromserver;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.weever.rotp_mih.client.ClientEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ChangeMaxUpStepPacket {
    public final int entityId;
    public final float maxUpStep;

    public ChangeMaxUpStepPacket(int entityId, float maxUpStep) {
        this.entityId = entityId;
        this.maxUpStep = maxUpStep;
    }

    public static class Handler implements IModPacketHandler<ChangeMaxUpStepPacket> {
        @Override
        public void encode(ChangeMaxUpStepPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeFloat(msg.maxUpStep);
        }

        @Override
        public ChangeMaxUpStepPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            float maxUpStep = buf.readFloat();
            return new ChangeMaxUpStepPacket(entityId, maxUpStep);
        }

        @Override
        public void handle(ChangeMaxUpStepPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    player.maxUpStep = msg.maxUpStep;
                }
            }
        }

        @Override
        public Class<ChangeMaxUpStepPacket> getPacketClass() {
            return ChangeMaxUpStepPacket.class;
        }
    }
}
