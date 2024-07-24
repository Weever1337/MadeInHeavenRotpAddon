package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TwoStepsBehind extends StandEntityAction {
    public TwoStepsBehind(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public ActionConditionResult checkTarget(ActionTarget target, LivingEntity user, IStandPower power) {
        Entity targetEntity = target.getEntity();
        if (targetEntity.is(power.getUser())) { return ActionConditionResult.NEGATIVE; }
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.ENTITY;
    }

    @Override
    public boolean standCanTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        return task.getTarget().getType() == ActionTarget.TargetType.ENTITY;
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        MihEntity MiH = (MihEntity) standEntity; // todo rewrite
        LivingEntity player = userPower.getUser();
        if (!world.isClientSide() && player != null) {
            if (!MiH.isValue(GameplayUtil.Values.ACCELERATION)) {
                ((PlayerEntity) player).displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.cant_use_without_timeaccel"), true);
                return;
            }
            Entity entity = task.getTarget().getEntity();
            Vector3d targetPos = getBackCoordinates((LivingEntity) entity, 1);
            double x = targetPos.x;
            double y = entity.getY();
            double z = targetPos.z;

            userPower.getUser().moveTo(x, y, z);
            userPower.getUser().lookAt(EntityAnchorArgument.Type.EYES, entity.getEyePosition(1));
            entity.hurt(DamageSource.playerAttack((PlayerEntity) userPower.getUser()), 5);
            if (!((LivingEntity) entity).hasEffect(InitEffects.BLEEDING.get())) {
                ((LivingEntity) entity).addEffect(new EffectInstance(InitEffects.BLEEDING.get(), 100, 1, false, false, true));
                ((LivingEntity) entity).addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_FACE.get(), 100, 1, false, false, true));
                ((LivingEntity) entity).addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1, false, false, true));
            }
        }
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        MihEntity MiH = (MihEntity) standEntity;
        if (!world.isClientSide()) {
            if (MiH.isValue(GameplayUtil.Values.ACCELERATION)) {
                world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_TWO_STEPS.get(), SoundCategory.PLAYERS,1,1);
            } else {
                standPower.setCooldownTimer(InitStands.MIH_TWO_STEPS.get(), 0);
            }
        }
    }

    public static Vector3d getBackCoordinates(LivingEntity entity, double distance) {
        Vector3d forward = entity.getLookAngle();
        Vector3d backward = forward.scale(-1);
        Vector3d backwardDistance = backward.scale(distance);

        double x = entity.getX() + backwardDistance.x;
        double y = entity.getY() + backwardDistance.y;
        double z = entity.getZ() + backwardDistance.z;
        return new Vector3d(x, y, z);
    }
}