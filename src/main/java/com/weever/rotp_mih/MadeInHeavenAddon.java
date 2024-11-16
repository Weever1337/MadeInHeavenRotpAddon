package com.weever.rotp_mih;

import com.weever.rotp_mih.addoncompat.OptionalDependencyHelper;
import com.weever.rotp_mih.init.InitCapabilities;
import com.weever.rotp_mih.init.InitEntities;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        modEventBus.addListener(this::onFMLCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MadeInHeavenConfig.clientSpec);
    }

    private void onFMLCommonSetup(FMLCommonSetupEvent event) {
        AddonPackets.init();
        InitCapabilities.registerCapabilities();
        OptionalDependencyHelper.init();
    }
}
