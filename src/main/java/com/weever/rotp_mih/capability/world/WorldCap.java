package com.weever.rotp_mih.capability.world;

import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.fromserver.SyncWorldCapPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class WorldCap {
    private final ServerWorld world;
    private UUID serverId;
    private UUID timeManipulatorUUID;
    private boolean isTimeManipulatorPlayer;
    private TimeData timeData = TimeData.NONE;
    private int timeAccelerationPhase = 0;
    private int tickCounter = 0;

    public WorldCap(ServerWorld world) {
        this.world = world;
        if (!world.isClientSide()) {
            this.serverId = UUID.randomUUID();
        }
    }

    public void tick() {
        if (!world.isClientSide()) {
            if ((timeManipulatorUUID == null && timeData != TimeData.NONE) || (isTimeManipulatorPlayer && world.getPlayerByUUID(Objects.requireNonNull(getTimeManipulatorUUID())) == null)) {
                setTimeManipulatorUUID(null, false);
                setTimeData(TimeData.NONE);
            }
            if (getTimeData() != TimeData.ACCELERATION && timeAccelerationPhase != 0) {
                setTimeAccelerationPhase(0);
                setTickCounter(0);
            }
        }
    }

    @Nullable
    public UUID getTimeManipulatorUUID() {
        return timeManipulatorUUID;
    }

    public void setTimeManipulatorUUID(UUID timeManipulatorUUID, boolean isTimeManipulatorPlayer) {
        this.timeManipulatorUUID = timeManipulatorUUID;
        this.isTimeManipulatorPlayer = isTimeManipulatorPlayer;
        if (!world.isClientSide()) {
            AddonPackets.sendToAllPlayers(SyncWorldCapPacket.timeManipulator(timeManipulatorUUID));
        }
    }

    public TimeData getTimeData() {
        return timeData;
    }

    public void setTimeData(TimeData timeData) {
        this.timeData = timeData;
        if (!world.isClientSide()) {
            AddonPackets.sendToAllPlayers(SyncWorldCapPacket.timeData(timeData));
        }
    }

    public int getTimeAccelerationPhase() {
        return timeAccelerationPhase;
    }

    public void setTimeAccelerationPhase(int timeAccelerationPhase) {
        this.timeAccelerationPhase = timeAccelerationPhase;
        if (!world.isClientSide()) {
            AddonPackets.sendToAllPlayers(SyncWorldCapPacket.timeAccelPhase(timeAccelerationPhase));
        }
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public void setTickCounter(int tickCounter) {
        this.tickCounter = tickCounter;
    }

    CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("ServerId", serverId);
        return nbt;
    }

    void load(CompoundNBT nbt) {
        if (nbt.hasUUID("ServerId")) {
            serverId = nbt.getUUID("ServerId");
        }
    }

    public void syncData(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            AddonPackets.sendToClient(SyncWorldCapPacket.bothData(getTimeManipulatorUUID(), getTimeData(), getTimeAccelerationPhase()), player);
        }
    }

    public enum TimeData {
        NONE, ACCELERATION
    }
}
