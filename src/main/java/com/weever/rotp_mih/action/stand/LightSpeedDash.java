package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
        if (ClientHandler.getClientTimeAccelPhase() < TimeUtil.GIVE_BUFFS) return ActionConditionResult.NEGATIVE;
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            sprinting = user.isSprinting();
            user.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 10, 99, false, false));
            user.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 10, 14, false, false));
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
            user.setSprinting(sprinting);
        }
    }
}