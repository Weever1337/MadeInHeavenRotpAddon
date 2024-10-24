package com.weever.rotp_mih;

import com.weever.rotp_mih.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MadeInHeavenAddon.MOD_ID)
public class MadeInHeavenAddon {
    public static final String MOD_ID = "rotp_mih";
    public static final Logger LOGGER = LogManager.getLogger();

    public MadeInHeavenAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
        InitParticles.PARTICLES.register(modEventBus);
    }
}
