package com.weever.rotp_mih.init;

import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.capability.world.WorldCapStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID)
public class InitCapabilities {
    private static final ResourceLocation WORLD_CAP = new ResourceLocation(MadeInHeavenAddon.MOD_ID, "world_cap");

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        if (!world.isClientSide() && world.dimension() == World.OVERWORLD) {
            event.addCapability(WORLD_CAP, new WorldCapProvider((ServerWorld) world));
        }
    }

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(WorldCap.class, new WorldCapStorage(), () -> new WorldCap(null));
    }
}
