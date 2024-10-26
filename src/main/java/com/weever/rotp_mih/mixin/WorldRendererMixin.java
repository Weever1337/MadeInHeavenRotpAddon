package com.weever.rotp_mih.mixin;

import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

//    @ModifyVariable(method = "renderSnowAndRain", at = @At("HEAD"), argsOnly = true, ordinal = 0)
//    private float jojoTsWeatherChangePartialTick(float partialTick) {
//        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
//            return TimeUtil.getCalculatedPhase();
//        }
//        return partialTick;
//    }
//
//    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), argsOnly = true, ordinal = 0)
//    private float jojoTsCloudsChangePartialTick(float partialTick) {
//        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
//            System.out.println("AAAA");
//            return 53535325f;
//        }
//        return 55325235f;
//    }
}
