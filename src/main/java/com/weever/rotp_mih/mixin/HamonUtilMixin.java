package com.weever.rotp_mih.mixin;

import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import com.weever.rotp_mih.utils.LiquidWalking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HamonUtil.class)
public class HamonUtilMixin {
    @Inject(method = "onLiquidWalkingEvent", at = @At("HEAD"), cancellable = true)
    private static void onLiquidWalkingEvent(LivingEntity entity, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (LiquidWalking.onLiquidWalkingEvent(entity, fluidState))
            cir.setReturnValue(true);
    }
}
