package com.weever.rotp_mih.client;

import com.github.standobyte.jojo.client.particle.MeteoriteVirusParticle;
import com.github.standobyte.jojo.client.particle.custom.CustomParticlesHelper;
import com.github.standobyte.jojo.client.resources.CustomResources;
import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.client.particle.CumParticle;
import com.weever.rotp_mih.client.particle.SparkParticle;
import com.weever.rotp_mih.client.render.entity.renderer.stand.MihRenderer;

import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpMadeInHeavenAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(
                InitStands.MIH.getEntityType(), MihRenderer::new);
    }

    @SubscribeEvent
    public static void onMcConstructor(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.register(InitParticles.SPARK.get(), SparkParticle.Factory::new);
        mc.particleEngine.register(InitParticles.CUM.get(), CumParticle.Factory::new);
        CustomParticlesHelper.saveSprites(mc);
        CustomResources.initCustomResourceManagers(mc);
    }
}
