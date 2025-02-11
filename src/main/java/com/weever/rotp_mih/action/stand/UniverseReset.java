package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;

public class UniverseReset extends StandEntityAction {
    public UniverseReset(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        super.checkStandConditions(stand, power, target);
        if (power.getStamina() < 300) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide) {
            LivingEntity user = userPower.getUser();
            if(user != null){
                world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(16 * 12), EntityPredicates.ENTITY_STILL_ALIVE).forEach(
                        livingEntity -> {
                            MCUtil.runCommand(livingEntity, "particle minecraft:ambient_entity_effect "+user.getX()+" "+user.getY() + 1 +" "+user.getZ()+" .5 .5 .5 1 30");
                            if (livingEntity == user || livingEntity == standEntity) {
                                livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 20 * 60, 4, false, false, true));
                                userPower.setCooldownTimer(InitStands.MIH_TIME_SYSTEM.get(), 20 * 60);
                            } else {
                                IStandPower.getStandPowerOptional(livingEntity).ifPresent(power -> {
                                    power.getAllUnlockedActions().forEach(ability -> power.setCooldownTimer(ability, 20 * 60));
                                    power.setStamina(0);
                                    if (!power.isActive()) {
                                        power.toggleSummon();
                                    }
                                    power.setLeapCooldown(20 * 60);
                                });

                                INonStandPower.getNonStandPowerOptional(livingEntity).ifPresent(nonPower -> {
//                                    if (nonPower.getType() == ModPowers.HAMON.get()) {
//                                        nonPower.setEnergy(0);
//                                    } else if (nonPower.getType() == ModPowers.VAMPIRISM.get()) {
//                                        if (nonPower.getEnergy() <= nonPower.getMaxEnergy() / 2) {
//                                            nonPower.setEnergy(nonPower.getMaxEnergy() / 2);
//                                        }
//                                    } TODO: Do it with an effect.
                                    nonPower.setLeapCooldown(20 * 60);
                                });
                                livingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 20 * 60, 10, false, false, true));
                            }
                        }
                );
            }
        }
    }
}