package com.weever.rotp_mih.mixin.client;

import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
@Deprecated
public class WorldRendererMixin {
    @ModifyVariable(method = "renderSnowAndRain", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsWeatherChangePartialTick(float partialTick) {
        if (TimeUtil.getTimeData(null) == WorldCap.TimeData.ACCELERATION) {
            return partialTick * TimeUtil.getCalculatedPhase(TimeUtil.getTimeAccelPhase(null));
        }
        return partialTick;
    }

    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsCloudsChangePartialTick(float partialTick) {
        if (TimeUtil.getTimeData(null) == WorldCap.TimeData.ACCELERATION) {
//            System.out.println("p: " + partialTick * TimeUtil.getCalculatedPhase(WorldCapProvider.getClientTimeAccelPhase()));
            return partialTick * TimeUtil.getCalculatedPhase(TimeUtil.getTimeAccelPhase(null));
        }
        return partialTick;
    }
}
