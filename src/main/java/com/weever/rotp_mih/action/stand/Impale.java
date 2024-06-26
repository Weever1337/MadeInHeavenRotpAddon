package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

public class Impale extends StandEntityHeavyAttack {
    public Impale(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        MihEntity MiH = (MihEntity) stand;
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        LivingEntity livingEntity = (LivingEntity) target;
        int duration = 100;
        if (!livingEntity.hasEffect(InitEffects.BLEEDING.get()));
            livingEntity.addEffect(new EffectInstance(InitEffects.BLEEDING.get(), duration, 1, false, false, true));
        livingEntity.addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_FACE.get(), duration*2, 1, false, false, true));
        livingEntity.addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_LEGS.get(), duration*2, 1, false, false, true));
        livingEntity.addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_ARMS.get(), duration*2, 1, false, false, true));

        return super.punchEntity(stand, target, dmgSource)
                .addKnockback(0.5F)
                .disableBlocking((float) stand.getProximityRatio(target) - 0.25F);
    }
}