package com.weever.rotp_mih.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class LivingUtilCap {
    private final LivingEntity entity;
    private float timeStopTicks;

    public LivingUtilCap(LivingEntity entity) {
        this.entity = entity;
    }

    public float getTimeStopTicks() {
        return timeStopTicks;
    }

    public void setTimeStopTicks(float timeStopTicks) {
        this.timeStopTicks = timeStopTicks;
    }

    public CompoundNBT toNBT() {
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT nbt) {
    }
}
