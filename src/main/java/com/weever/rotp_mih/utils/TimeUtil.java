package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCap.TimeData;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TimeUtil {
    public static int GIVE_BUFFS = 4;

    public static void multiplyProjectileSpeed(ProjectileEntity projectile, int n) {
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(n));
    }

    public static int getCalculatedPhase(int phase) {
        int multiply;
        switch (phase) {
            case 0:
            case 1: case 2: case 3: case 4: case 5:
                multiply = 2;
                break;
            case 6: case 7: case 8: case 9: case 10:
                multiply = 3;
                break;
            case 11: case 12: case 13: case 14: case 15:
                multiply = 4;
                break;
            case 16: case 17: case 18: case 19: case 20:
                multiply = 5;
                break;
            case 21: case 22: case 23: case 24: case 25:
                multiply = 6;
                break;
            case 26: case 27: case 28: case 29: case 30:
                multiply = 7;
                break;
            default:
                multiply = 1;
                break;
        }
        return multiply;
    }

    public static boolean checkConditions(LivingEntity user, IStandPower power, boolean timeSystem) {
        boolean checkForTimeData = TimeUtil.getTimeData(user.level) == TimeData.NONE && !timeSystem;
        if (TimeUtil.getTimeData(user.level) != TimeData.NONE && customEqualUUID(user.getUUID(), user.level)) checkForTimeData = true;
        return power.getType() == InitStands.MADE_IN_HEAVEN.getStandType() &&
                user.level.dimension() == World.OVERWORLD &&
                user.level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)
                && checkForTimeData;
    }

    public static boolean equalUUID(UUID oneUuid, UUID twoUuid) {
        if (oneUuid == null || twoUuid == null) return false;
        return (oneUuid.equals(twoUuid) || oneUuid == twoUuid);
    }

    public static boolean customEqualUUID(UUID uuid, @Nullable World world) {
        if (world == null) {
            return equalUUID(ClientHandler.getClientTimeManipulatorUUID(), uuid);
        } else {
            if (!world.isClientSide()) {
                try {
                    return equalUUID(WorldCapProvider.getWorldCap((ServerWorld) world).getTimeManipulatorUUID(), uuid);
                } catch (IllegalArgumentException exception) {
                    MadeInHeavenAddon.LOGGER.error("This world dont have a capability? Interesting.");
                    return false;
                }
            } else {
                return equalUUID(ClientHandler.getClientTimeManipulatorUUID(), uuid);
            }
        }
    }

    public static TimeData getTimeData(@Nullable World world) {
        if (world == null) {
            return ClientHandler.getClientTimeData();
        } else {
            if (!world.isClientSide()) {
                try {
                    return WorldCapProvider.getWorldCap((ServerWorld) world).getTimeData();
                } catch (IllegalArgumentException exception) {
                    MadeInHeavenAddon.LOGGER.error("This world dont have a capability? Interesting.");
                    return TimeData.NONE;
                }
            } else {
                return ClientHandler.getClientTimeData();
            }
        }
    }

    public static UUID getTimeManipulator(@Nullable World world) {
        if (world == null) {
            return ClientHandler.getClientTimeManipulatorUUID();
        } else {
            if (!world.isClientSide()) {
                try {
                    return WorldCapProvider.getWorldCap((ServerWorld) world).getTimeManipulatorUUID();
                } catch (IllegalArgumentException exception) {
                    MadeInHeavenAddon.LOGGER.error("This world dont have a capability? Interesting.");
                    return UUID.randomUUID();
                }
            } else {
                return ClientHandler.getClientTimeManipulatorUUID();
            }
        }
    }

    public static int getTimeAccelPhase(@Nullable World world) {
        if (world == null) {
            return ClientHandler.getClientTimeAccelPhase();
        } else {
            if (!world.isClientSide()) {
                try {
                    return WorldCapProvider.getWorldCap((ServerWorld) world).getTimeAccelerationPhase();
                } catch (IllegalArgumentException exception) {
                    MadeInHeavenAddon.LOGGER.error("This world dont have a capability? Interesting.");
                    return 0;
                }
            } else {
                return ClientHandler.getClientTimeAccelPhase();
            }
        }
    }
}
