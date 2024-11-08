package com.weever.rotp_mih.mixin.client;

import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyVariable(method = "renderSnowAndRain", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsWeatherChangePartialTick(float partialTick) {
        if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION) {
            return partialTick * TimeUtil.getCalculatedPhase(WorldCapProvider.getClientTimeAccelPhase());
        }
        return partialTick;
    }

    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float jojoTsCloudsChangePartialTick(float partialTick) {
        if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION) {
//            System.out.println("p: " + partialTick * TimeUtil.getCalculatedPhase(WorldCapProvider.getClientTimeAccelPhase()));
            return partialTick * TimeUtil.getCalculatedPhase(WorldCapProvider.getClientTimeAccelPhase());
        }
        return partialTick;
    }
}
