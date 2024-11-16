package com.weever.rotp_mih.capability.world;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

public class WorldCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(WorldCap.class)
    public static Capability<WorldCap> CAPABILITY = null;
    private LazyOptional<WorldCap> instance;

    public WorldCapProvider(ServerWorld world) {
        this.instance = LazyOptional.of(() -> new WorldCap(world));
    }

    private static UUID clientTimeManipulatorUUID = null;
    private static WorldCap.TimeData clientTimeData = WorldCap.TimeData.NONE;
    private static int clientTimeAccelPhase = 0;

    public static void setClientData(UUID timeManipulatorUUID, WorldCap.TimeData timeData, int timeAccelPhase) {
        clientTimeManipulatorUUID = timeManipulatorUUID;
        clientTimeData = timeData;
        clientTimeAccelPhase = timeAccelPhase;
    }

    public static UUID getClientTimeManipulatorUUID() {
        return clientTimeManipulatorUUID;
    }

    public static void setClientTimeManipulatorUUID(UUID timeManipulatorUUID) {
        clientTimeManipulatorUUID = timeManipulatorUUID;
    }

    public static WorldCap.TimeData getClientTimeData() {
        return clientTimeData;
    }

    public static void setClientTimeData(WorldCap.TimeData timeData) {
        clientTimeData = timeData;
    }

    public static int getClientTimeAccelPhase() {
        return clientTimeAccelPhase;
    }

    public static void setClientTimeAccelPhase(int timeAccelPhase) {
        clientTimeAccelPhase = timeAccelPhase;
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
                () -> new IllegalArgumentException("Save file capability LazyOptional is not attached."));
    }

    public static WorldCap getWorldCap(ServerWorld world) {
        return world.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalArgumentException("Save file capability LazyOptional is not attached."));
    }

    public static WorldCap getWorldCap(ServerPlayerEntity serverPlayer) {
        return getWorldCap(serverPlayer.server);
    }
}
