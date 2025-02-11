package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.capability.entity.PlayerUtilCap;
import com.github.standobyte.jojo.capability.entity.PlayerUtilCapProvider;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonSkills;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;

public class LiquidWalking {
    public static boolean onLiquidWalkingEvent(LivingEntity entity, FluidState fluidState) {
        return isLiquidWalking(entity, fluidState);
    }

    private static boolean isLiquidWalking(LivingEntity entity, FluidState fluidState) {
        boolean doubleShift = entity.isShiftKeyDown() && entity.getCapability(PlayerUtilCapProvider.CAPABILITY).map(
                PlayerUtilCap::getDoubleShiftPress).orElse(false);

        if (doubleShift) {
            return false;
        }

        if (INonStandPower.getNonStandPowerOptional(entity).map(nonPower -> nonPower.getType() == ModPowers.HAMON.get() && nonPower.getTypeSpecificData(ModPowers.HAMON.get()).map(hamon -> hamon.isSkillLearned(ModHamonSkills.LIQUID_WALKING.get())).orElse(false) && nonPower.getEnergy() > 0).orElse(false)) {
            return false;
        }

        return IStandPower.getStandPowerOptional(entity).map(power -> {
            if (TimeUtil.customEqualUUID(entity.getUUID(), entity.level) && TimeUtil.getTimeData(entity.level) == WorldCap.TimeData.ACCELERATION && TimeUtil.getTimeAccelPhase(entity.level) >= TimeUtil.GIVE_BUFFS) {
                if (power.getType() == InitStands.MADE_IN_HEAVEN.getStandType() && power.isActive()) {
                    if (entity.isSprinting() && !entity.isCrouching()) {
                        if (power.getStamina() > 0) {
                            if (!entity.level.isClientSide()) {
                                entity.setOnGround(true);
                                if (fluidState.getType().is(FluidTags.LAVA) && !entity.fireImmune()) {
                                    entity.hurt(DamageSource.HOT_FLOOR, 1.0f);
                                }
                                power.consumeStamina(2f);
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }).orElse(false);
    }
}
