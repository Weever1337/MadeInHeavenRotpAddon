package com.weever.rotp_mih.capability;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

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
        System.out.println("haha");
        if (!world.isClientSide()) {
            if ((timeManipulatorUUID == null && timeData != TimeData.NONE) || (timeManipulatorUUID != null && world.getPlayerByUUID(Objects.requireNonNull(timeManipulatorUUID, "wtf? sync timeManipulatorUUID")) == null)) {
                timeManipulatorUUID = null;
                tickCounter=0;
            }
            if (timeData != TimeData.ACCELERATION && timeAccelerationPhase != 0) {
                timeAccelerationPhase = 0;
                tickCounter=0;
            }
        }
    }

    public UUID getTimeManipulatorUUID() {
        if (timeManipulatorUUID == null) return UUID.randomUUID();
        return timeManipulatorUUID;
    }

    public void setTimeManipulatorUUID(UUID timeManipulatorUUID) {
        this.timeManipulatorUUID = timeManipulatorUUID;
    }

    public TimeData getTimeData() {
        return timeData;
    }

    public void setTimeData(TimeData timeData) {
        this.timeData = timeData;
    }

    public int getTimeAccelerationPhase() {
        return timeAccelerationPhase;
    }

    public void setTimeAccelerationPhase(int timeAccelerationPhase) {
        this.timeAccelerationPhase = timeAccelerationPhase;
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

    public enum TimeData {
        NONE, ACCELERATION
    }
}
