package com.weever.rotp_mih.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class LivingUtilProvider implements ICapabilitySerializable<INBT>{
    @CapabilityInject(LivingUtilCap.class)
    public static Capability<LivingUtilCap> CAPABILITY = null;
    private LazyOptional<LivingUtilCap> instance;

    public LivingUtilProvider(LivingEntity entity) {
        this.instance = LazyOptional.of(() -> new LivingUtilCap(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Living capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Living capability LazyOptional is not attached.")), null, nbt);
    }
}
