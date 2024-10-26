package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.WorldCap;
import com.weever.rotp_mih.capability.WorldCapProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class LightSpeedDash extends StandEntityAction {
    public LightSpeedDash(Builder builder) {
        super(builder.holdType(3));
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if (power.getStamina() > 50) return ActionConditionResult.POSITIVE;
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
//            if (!MadeInHeavenStandType.isValue(TimeUtil.Values.ACCELERATION)) {
//                ((PlayerEntity) user).displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.cant_use_without_timeaccel"), true);
//                return;
//            }
            WorldCapProvider.getWorldCap((ServerPlayerEntity) user).setTimeManipulatorUUID(user.getUUID());
            WorldCapProvider.getWorldCap((ServerPlayerEntity) user).setTimeData(WorldCap.TimeData.ACCELERATION);
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

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        if (!world.isClientSide()) {
            standPower.getType();
//            if (MadeInHeavenStandType.isValue(TimeUtil.Values.ACCELERATION)) {
//                world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_DASH.get(), SoundCategory.PLAYERS,1,1);
//                world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_DASH_USER.get(), SoundCategory.PLAYERS,1,1);
//            } else {
//                standPower.setCooldownTimer(InitStands.MIH_DASH.get(), 0);
//            }
        }
    }
}