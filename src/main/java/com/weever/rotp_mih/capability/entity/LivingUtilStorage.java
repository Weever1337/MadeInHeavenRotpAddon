package com.weever.rotp_mih.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class LivingUtilStorage implements Capability.IStorage<LivingUtilCap> {
    @Override
    public INBT writeNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
