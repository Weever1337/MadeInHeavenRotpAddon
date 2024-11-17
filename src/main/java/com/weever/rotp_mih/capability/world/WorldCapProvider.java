package com.weever.rotp_mih.capability.world;

import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(WorldCap.class)
    public static Capability<WorldCap> CAPABILITY = null;
    private LazyOptional<WorldCap> instance;

    public WorldCapProvider(ServerWorld world) {
        this.instance = LazyOptional.of(() -> new WorldCap(world));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null, nbt);
    }

    public static WorldCap getWorldCap(MinecraftServer server) {
        return server.overworld().getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached."));
    }

    public static WorldCap getWorldCap(ServerWorld world) {
        return world.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached."));
    }
}
