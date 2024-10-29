package com.weever.rotp_mih.client;

import com.github.standobyte.jojo.client.particle.custom.CustomParticlesHelper;
import com.github.standobyte.jojo.client.resources.CustomResources;
import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.client.particle.SparkParticle;
import com.weever.rotp_mih.client.render.MadeInHeavenRenderer;
import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();

        RenderingRegistry.registerEntityRenderingHandler(
                InitStands.MADE_IN_HEAVEN.getEntityType(), MadeInHeavenRenderer::new);
        StandStatsRenderer.overrideCosmeticStats(
                InitStands.MADE_IN_HEAVEN.getStandType().getRegistryName(),
                new StandStatsRenderer.ICosmeticStandStats() {
                    @Override
                    public String statRankLetter(StandStatsRenderer.StandStat stat, IStandPower standData, double statConvertedValue) {
                        if (stat == StandStatsRenderer.StandStat.SPEED) {
                            return "∞";
                        }
                        return StandStatsRenderer.ICosmeticStandStats.super.statRankLetter(stat, standData, statConvertedValue);
                    }
                });
        ClientEventHandler.init(mc);
    }

    @SubscribeEvent
    public static void onMcConstructor(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.register(InitParticles.SPARK.get(), SparkParticle.Factory::new);
        CustomParticlesHelper.saveSprites(mc);
        CustomResources.initCustomResourceManagers(mc);
    }
}
