package com.weever.rotp_mih.network.fromserver;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncWorldCapPacket {
    private final PacketField field;
    private final UUID timeManipulatorUUID;
    private final WorldCap.TimeData timeData;
    private final int timeAccelPhase;

    public SyncWorldCapPacket(PacketField field, UUID timeManipulatorUUID, WorldCap.TimeData timeData, int timeAccelPhase) {
        this.field = field;
        this.timeManipulatorUUID = timeManipulatorUUID;
        this.timeData = timeData;
        this.timeAccelPhase = timeAccelPhase;
    }

    public static SyncWorldCapPacket timeManipulator(UUID timeManipulatorUUID) {
        return new SyncWorldCapPacket(PacketField.TIME_MANIPULATOR, timeManipulatorUUID, null, 0);
    }

    public static SyncWorldCapPacket timeData(WorldCap.TimeData timeData) {
        return new SyncWorldCapPacket(PacketField.TIME_DATA, null, timeData, 0);
    }

    public static SyncWorldCapPacket timeAccelPhase(int timeAccelPhase) {
        return new SyncWorldCapPacket(PacketField.TIME_ACCEL_PHASE, null, null, timeAccelPhase);
    }

    public static SyncWorldCapPacket bothData(UUID timeManipulatorUUID, WorldCap.TimeData timeData, int timeAccelPhase) {
        return new SyncWorldCapPacket(PacketField.BOTH, timeManipulatorUUID, timeData, timeAccelPhase);
    }

    public static class Handler implements IModPacketHandler<SyncWorldCapPacket> {
        @Override
        public void encode(SyncWorldCapPacket msg, PacketBuffer buffer) {
            buffer.writeEnum(msg.field);
            switch (msg.field) {
                case TIME_MANIPULATOR:
                    buffer.writeBoolean(msg.timeManipulatorUUID != null);
                    if (msg.timeManipulatorUUID != null) {
                        buffer.writeUUID(msg.timeManipulatorUUID);
                    }
                    break;
                case TIME_DATA:
                    buffer.writeEnum(msg.timeData);
                    break;
                case TIME_ACCEL_PHASE:
                    buffer.writeInt(msg.timeAccelPhase);
                    break;
                case BOTH:
                    buffer.writeBoolean(msg.timeManipulatorUUID != null);
                    if (msg.timeManipulatorUUID != null) {
                        buffer.writeUUID(msg.timeManipulatorUUID);
                    }
                    buffer.writeEnum(msg.timeData);
                    break;
            }
        }

        @Override
        public SyncWorldCapPacket decode(PacketBuffer buf) {
            PacketField field = buf.readEnum(PacketField.class);
            switch (field) {
                case TIME_MANIPULATOR:
                    boolean hasUUID = buf.readBoolean();
                    UUID timeManipulatorUUID = hasUUID ? buf.readUUID() : null;
                    return new SyncWorldCapPacket(field, timeManipulatorUUID, null, 0);
                case TIME_DATA:
                    WorldCap.TimeData timeData = buf.readEnum(WorldCap.TimeData.class);
                    return new SyncWorldCapPacket(field, null, timeData, 0);
                case TIME_ACCEL_PHASE:
                    int timeAccelPhase = buf.readInt();
                    return new SyncWorldCapPacket(field, null, null, timeAccelPhase);
                case BOTH:
                    boolean hasBothUUID = buf.readBoolean();
                    UUID manipulator = hasBothUUID ? buf.readUUID() : null;
                    WorldCap.TimeData timetdata = buf.readEnum(WorldCap.TimeData.class);
                    int clTimeAccelPhase = buf.readInt();
                    return new SyncWorldCapPacket(field, manipulator, timetdata, clTimeAccelPhase);
            }
            throw new IllegalStateException("Unknown field for Packet SyncWorldCapPacket: " + field);
        }

        @Override
        public void handle(SyncWorldCapPacket msg, Supplier<NetworkEvent.Context> ctx) {
            switch (msg.field) {
                case TIME_MANIPULATOR:
                    WorldCapProvider.setClientTimeManipulatorUUID(msg.timeManipulatorUUID);
                    break;
                case TIME_DATA:
                    WorldCapProvider.setClientTimeData(msg.timeData);
                    break;
                case TIME_ACCEL_PHASE:
                    WorldCapProvider.setClientTimeAccelPhase(msg.timeAccelPhase);
                    break;
                case BOTH:
                    WorldCapProvider.setClientData(msg.timeManipulatorUUID, msg.timeData, msg.timeAccelPhase);
                    break;
            }
        }

        @Override
        public Class<SyncWorldCapPacket> getPacketClass() {
            return SyncWorldCapPacket.class;
        }
    }

    public enum PacketField {
        TIME_DATA,
        TIME_MANIPULATOR,
        TIME_ACCEL_PHASE,
        BOTH
    }
}
