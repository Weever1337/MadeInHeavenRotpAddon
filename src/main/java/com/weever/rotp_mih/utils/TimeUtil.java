package com.weever.rotp_mih.utils;

import com.weever.rotp_mih.data.TimeSystemData;
import net.minecraft.entity.projectile.ProjectileEntity;

import java.util.UUID;

public class TimeUtil {
    private static TimeSystemData globalValue = new TimeSystemData(null, Values.NONE);
    public enum Values {
        NONE,
        ACCELERATION
    }

    public static void setGlobalValue(UUID player, Values val) {
        globalValue = new TimeSystemData(player, val);
    }

    public static TimeSystemData getGlobalValue() {
        return globalValue;
    }

    public static int timeAccelPhase = 1;

    public static void multiplyProjectileSpeed(ProjectileEntity projectile, int n) {
        double motionX = projectile.getDeltaMovement().x;
        double motionY = projectile.getDeltaMovement().y;
        double motionZ = projectile.getDeltaMovement().z;

        projectile.setDeltaMovement(motionX * n, motionY * n, motionZ * n);
    }

    public static int getCalculatedPhase() {
        int phase = timeAccelPhase;
        int multiply;
        switch (phase) {
            case 1: case 2: case 3: case 4: case 5:
                multiply = 2;
                break;
            case 6: case 7: case 8: case 9: case 10:
                multiply = 3;
                break;
            case 11: case 12: case 13: case 14: case 15:
                multiply = 4;
                break;
            default:
                multiply = 1;
                break;
        }
        return multiply;
    }
}
