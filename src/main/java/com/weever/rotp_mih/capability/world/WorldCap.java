package com.weever.rotp_mih.capability.world;

import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.fromserver.ServerIdPacket;
import com.weever.rotp_mih.network.fromserver.SyncWorldCapPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class WorldCap {
    private final ServerWorld world;
    private UUID serverId;
    private UUID timeManipulatorUUID;
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
            if ((timeManipulatorUUID == null && timeData != TimeData.NONE) || (timeManipulatorUUID != null && world.getPlayerByUUID(Objects.requireNonNull(timeManipulatorUUID, "wtf? sync timeManipulatorUUID")) == null)) {
                setTimeManipulatorUUID(null);
                setTimeData(TimeData.NONE);
            }
            if (timeData != TimeData.ACCELERATION && timeAccelerationPhase != 0) {
                setTimeAccelerationPhase(0);
                tickCounter = 0;
            }
        }
    }

    @Nullable
    public UUID getTimeManipulatorUUID() {
        return timeManipulatorUUID;
    }

    public void setTimeManipulatorUUID(UUID timeManipulatorUUID) {
        this.timeManipulatorUUID = timeManipulatorUUID;
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
        if (timeManipulatorUUID != null) {
            nbt.putUUID("TimeManipulatorUUID", timeManipulatorUUID);
        }
        MCUtil.nbtPutEnum(nbt, "TimeData", timeData);

        return nbt;
    }

    void load(CompoundNBT nbt) {
        if (nbt.hasUUID("ServerId")) {
            serverId = nbt.getUUID("ServerId");
        }
        if (nbt.hasUUID("TimeManipulatorUUID")) {
            timeManipulatorUUID = nbt.getUUID("TimeManipulatorUUID");
        }
        timeData = MCUtil.nbtGetEnum(nbt, "TimeData", TimeData.class);
    }

    public void onPlayerLogIn(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new ServerIdPacket(serverId), player);
    }

    public enum TimeData {
        NONE, ACCELERATION
    }
}
