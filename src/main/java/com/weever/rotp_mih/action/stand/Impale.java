package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class Impale extends StandEntityHeavyAttack {
    public Impale(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        LivingEntity livingEntity = (LivingEntity) target;
        int duration = 100;
        if (!livingEntity.hasEffect(InitEffects.BLEEDING.get()))
            livingEntity.addEffect(new EffectInstance(InitEffects.BLEEDING.get(), duration, 1, false, false, true));
        return super.punchEntity(stand, target, dmgSource)
                .addKnockback(0.5F)
                .disableBlocking((float) stand.getProximityRatio(target) - 0.25F);
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        standEntity.alternateHands();
        if (!world.isClientSide()) {
            standEntity.addFinisherMeter(-0.51F, 0);
            world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_IMPALE.get(), SoundCategory.PLAYERS,1,1);
        }
    }
}