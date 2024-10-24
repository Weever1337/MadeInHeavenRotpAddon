package com.weever.rotp_mih.mixin;

import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @ModifyVariable(method = "renderSnowAndRain", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsWeatherChangePartialTick(float partialTick) {
        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
            return TimeUtil.getCalculatedPhase();
        }
        return partialTick;
    }

    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsCloudsChangePartialTick(float partialTick) {
        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
            System.out.println("AAAA");
            return 53535325f;
        }
        return 55325235f;
    }
}
