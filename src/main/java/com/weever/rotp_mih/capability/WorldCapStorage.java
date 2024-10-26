package com.weever.rotp_mih.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class WorldCapStorage implements IStorage<WorldCap> {
    @Override
    public INBT writeNBT(Capability<WorldCap> capability, WorldCap instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("ServerData", instance.save());
        return nbt;
    }

    @Override
    public void readNBT(Capability<WorldCap> capability, WorldCap instance, Direction side, INBT nbt) {
        CompoundNBT cnbt = (CompoundNBT) nbt;
        instance.load(cnbt.getCompound("ServerData"));
    }
}