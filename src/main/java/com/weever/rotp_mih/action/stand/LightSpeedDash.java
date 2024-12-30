package com.weever.rotp_mih.action.stand;

import java.util.List;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.AfterimageEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_mih.utils.TimeUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class LightSpeedDash extends StandEntityAction {
    public LightSpeedDash(Builder builder) {
        super(builder);
    }
    boolean sprinting;

    @Override
    public ActionConditionResult checkConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!TimeUtil.checkConditions(user, power, true))
            return ActionConditionResult.createNegative(new TranslationTextComponent("rotp_mih.message.action_condition.cant_use_without_acceleration"));
        if (TimeUtil.getTimeAccelPhase(user.level) < TimeUtil.GIVE_BUFFS) return ActionConditionResult.NEGATIVE;
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void onClick(World world, LivingEntity user, IStandPower power) {
        sprinting = user.isSprinting();
        if (!world.isClientSide()) {
        	AfterimageEntity afterImage = new AfterimageEntity(world, user, 1) {
        		@Override
        		public void tick() {
        			super.tick();
        			List<LivingEntity> aroundEntities = MCUtil.entitiesAround(LivingEntity.class, this, 2, false, entity -> !entity.is(user) && entity.isAlive());
        			for (LivingEntity entity : aroundEntities) {
        				entity.hurt(DamageSource.GENERIC, 1.0f);
        			}
        		}
        	};
        	afterImage.setLifeSpan(5);
        	world.addFreshEntity(afterImage);
        }
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
        	
            user.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 10, 14, false, false));
            user.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 10, TimeUtil.getCalculatedPhase(TimeUtil.getTimeAccelPhase(world)) * 10, false, false));
            user.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 10, 14, false, false));
        }
    }

    @Override
    protected void onTaskStopped(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task, StandEntityAction newAction) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            user.removeEffect(Effects.DAMAGE_RESISTANCE);
            user.removeEffect(Effects.MOVEMENT_SPEED);
            user.removeEffect(Effects.DOLPHINS_GRACE);

            sprinting = false;
        }
    }
}