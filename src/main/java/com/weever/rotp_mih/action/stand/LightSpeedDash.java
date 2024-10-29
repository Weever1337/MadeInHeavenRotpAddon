package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class LightSpeedDash extends StandEntityAction {
    public LightSpeedDash(Builder builder) {
        super(builder.holdType(3));
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        super.checkStandConditions(stand, power, target);
        LivingEntity user = power.getUser();
        if (user.level.dimension() != World.OVERWORLD) return ActionConditionResult.NEGATIVE;
        if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION && UUIDUtil.equals(user.getUUID(), WorldCapProvider.getClientTimeManipulatorUUID()))
            return ActionConditionResult.POSITIVE;
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            user.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 10, 99, false, false));
            user.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 10, 4, false, false));
            user.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 10, 19, false, false));
        }
    }

    @Override
    protected void onTaskStopped(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task, StandEntityAction newAction) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            user.removeEffect(Effects.MOVEMENT_SPEED);
            user.removeEffect(Effects.DAMAGE_RESISTANCE);
            user.removeEffect(Effects.DOLPHINS_GRACE);
        }
    }
}