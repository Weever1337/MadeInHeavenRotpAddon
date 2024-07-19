package com.weever.rotp_mih.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class UniverseResetData { // Soon
    private final LivingEntity player;
    private final Vector3d position;

    public UniverseResetData(LivingEntity player, Vector3d position) {
        this.player = player;
        this.position = position;
    }

    public LivingEntity getPlayer() {
        return player;
    }

    public Vector3d getPosition() {
        return position;
    }
}