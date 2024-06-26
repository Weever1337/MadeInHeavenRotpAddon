package com.weever.rotp_mih;

import com.weever.rotp_mih.init.*;
import com.weever.rotp_mih.network.AddonPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RotpMadeInHeavenAddon.MOD_ID)
public class RotpMadeInHeavenAddon {
    public static final String MOD_ID = "rotp_mih";
    private static final Logger LOGGER = LogManager.getLogger();

    public RotpMadeInHeavenAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
        InitParticles.PARTICLES.register(modEventBus);

        AddonPackets.init();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
