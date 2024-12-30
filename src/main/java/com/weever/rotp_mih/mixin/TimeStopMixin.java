package com.weever.rotp_mih.mixin;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.action.stand.TimeSystem;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.utils.TimeUtil;

import net.minecraft.entity.LivingEntity;

@Mixin(TimeStop.class)
public abstract class TimeStopMixin {
    @Inject(method = "canUserSeeInStoppedTime(Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/stand/IStandPower;)Z", at = @At("RETURN"), cancellable = true)
    private void onCanUserSeeInStoppedTime(LivingEntity user, IStandPower power, CallbackInfoReturnable<Boolean> cir) {
        if (TimeUtil.customEqualUUID(user.getUUID(), null) && power.getType() == InitStands.MADE_IN_HEAVEN.getStandType()) {
            if (TimeUtil.getTimeData(user.level) == WorldCap.TimeData.ACCELERATION) {
                if (TimeUtil.getTimeAccelPhase(user.level) > TimeUtil.GIVE_BUFFS) {
                	AtomicBoolean canSee = new AtomicBoolean(false);
                	power.getAllUnlockedActions().forEach(action -> {
                		if (action instanceof TimeSystem) {
                			canSee.set(((TimeSystem) action).canSeeInTimestopWhileAcceleration);
                		}
                	});
                    cir.setReturnValue(canSee.get());
                }
            }
        }
    }
}