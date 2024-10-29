package com.weever.rotp_mih.init;

import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.entity.LivingUtilCap;
import com.weever.rotp_mih.capability.entity.LivingUtilProvider;
import com.weever.rotp_mih.capability.entity.LivingUtilStorage;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.capability.world.WorldCapStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID)
public class InitCapabilities {
    private static final ResourceLocation WORLD_CAP = new ResourceLocation(MadeInHeavenAddon.MOD_ID, "world_cap");
    private static final ResourceLocation LIVING_UTIL = new ResourceLocation(MadeInHeavenAddon.MOD_ID, "living_util");

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        if (!world.isClientSide() && world.dimension() == World.OVERWORLD) {
            event.addCapability(WORLD_CAP, new WorldCapProvider((ServerWorld) world));
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<LivingEntity> event) {
        LivingEntity livingEntity = event.getObject();
        event.addCapability(LIVING_UTIL, new LivingUtilProvider(livingEntity));
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        WorldCapProvider.getWorldCap(player).onPlayerLogIn(player);
    }

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(LivingUtilCap.class, new LivingUtilStorage(), () -> new LivingUtilCap(null));
        CapabilityManager.INSTANCE.register(WorldCap.class, new WorldCapStorage(), () -> new WorldCap(null));
    }
}
