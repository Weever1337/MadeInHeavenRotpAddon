package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandBlockPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.weever.rotp_mih.init.InitSounds;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Chop extends StandEntityHeavyAttack {
    public Chop(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) target;
            int duration = 100;
            int amplifier = 1;

            EffectInstance existingEffect = livingEntity.getEffect(ModStatusEffects.BLEEDING.get());
            if (existingEffect != null) {
                duration += existingEffect.getDuration();
                amplifier = Math.min(existingEffect.getAmplifier() + 1, 5);
            }
            livingEntity.addEffect(new EffectInstance(ModStatusEffects.BLEEDING.get(), duration, amplifier, false, false, true));
        }
        return new MIHHeavyPunchInstance(stand, target, dmgSource)
                .copyProperties(super.punchEntity(stand, target, dmgSource))
                .addKnockback(0.5F)
                .disableBlocking((float) stand.getProximityRatio(target) - 0.25F);
    }
    
    @Override
    public StandBlockPunch punchBlock(StandEntity stand, BlockPos pos, BlockState state, Direction face) {
        return new MIHHeavyBlockInstance(stand, pos, state, face);
    }


    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        standEntity.alternateHands();
        if (!world.isClientSide()) {
            standEntity.addFinisherMeter(-0.51F, 0);
            world.playSound(null, standEntity.blockPosition(), InitSounds.MIH_CHOP.get(), SoundCategory.VOICE, 1, 1);
        }
    }
    
    public static class MIHHeavyPunchInstance extends HeavyPunchInstance {

        public MIHHeavyPunchInstance(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
            super(stand, target, dmgSource);
        }

        @Override
        protected void afterAttack(StandEntity stand, Entity target, StandEntityDamageSource dmgSource, StandEntityTask task, boolean hurt, boolean killed) {
        }
    }
    
    public static class MIHHeavyBlockInstance extends HeavyPunchBlockInstance {
        public MIHHeavyBlockInstance(StandEntity stand, BlockPos targetPos, BlockState blockState, Direction face) {
            super(stand, targetPos, blockState, face);
        }

        @Override
        public boolean doHit(StandEntityTask task) {
			return false;
        }
    }
}