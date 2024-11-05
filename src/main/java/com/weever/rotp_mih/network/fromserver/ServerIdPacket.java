package com.weever.rotp_mih.network.fromserver;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.weever.rotp_mih.client.ClientEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerIdPacket {
    public final int serverId;

    public ServerIdPacket(int serverId) {
        this.serverId = serverId;
    }

    public static class Handler implements IModPacketHandler<ServerIdPacket> {
        @Override
        public void encode(ServerIdPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.serverId);
        }

        @Override
        public ServerIdPacket decode(PacketBuffer buf) {
            int serverId = buf.readInt();
            return new ServerIdPacket(serverId);
        }

        @Override
        public void handle(ServerIdPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ClientEventHandler.getInstance().setServerId(msg);
        }

        @Override
        public Class<ServerIdPacket> getPacketClass() {
            return ServerIdPacket.class;
        }
    }
}
