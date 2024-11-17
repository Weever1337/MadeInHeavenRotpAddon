package com.weever.rotp_mih.mixin;

import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TimeStop.class)
public abstract class TimeStopMixin {
    @Inject(method = "canUserSeeInStoppedTime(Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/stand/IStandPower;)Z", at = @At("RETURN"), cancellable = true)
    private void onCanUserSeeInStoppedTime(LivingEntity user, IStandPower power, CallbackInfoReturnable<Boolean> cir) {
        if (TimeUtil.equalUUID(user.getUUID()) && power.getType() == InitStands.MADE_IN_HEAVEN.getStandType()) {
            if (ClientHandler.getClientTimeData() != WorldCap.TimeData.ACCELERATION) {
                cir.setReturnValue(false);
            } else {
                if (ClientHandler.getClientTimeAccelPhase() < TimeUtil.GIVE_BUFFS) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}