package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.world.WorldCap.TimeData;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.UUID;

public class TimeUtil {
    public static void multiplyProjectileSpeed(ProjectileEntity projectile, int n) {
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(n));
    }

    public static int getCalculatedPhase(int phase) {
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

    public static boolean checkConditions(LivingEntity user, IStandPower power, boolean specificAbility) {
        boolean checkForTimeData = WorldCapProvider.getClientTimeData() == TimeData.NONE && !specificAbility;
        if (WorldCapProvider.getClientTimeData() != TimeData.NONE && equalUUID(user.getUUID())) checkForTimeData = true;
        return power.getType() == InitStands.MADE_IN_HEAVEN.getStandType() &&
                user.level.dimension() == World.OVERWORLD &&
                user.level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)
                && checkForTimeData;
    }

    public static boolean equalUUID(UUID oneUuid, UUID twoUuid) {
        if (oneUuid == null || twoUuid == null) return false;
        return (oneUuid.equals(twoUuid) || oneUuid == twoUuid);
    }

    public static boolean equalUUID(UUID uuid) {
        return equalUUID(WorldCapProvider.getClientTimeManipulatorUUID(), uuid);
    }
}
