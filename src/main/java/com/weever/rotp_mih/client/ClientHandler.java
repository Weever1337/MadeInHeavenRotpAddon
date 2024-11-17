package com.weever.rotp_mih.client;

import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.fromclient.SyncClientWorldCapPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class ClientHandler {
    private static UUID clientTimeManipulatorUUID = null;
    private static WorldCap.TimeData clientTimeData = WorldCap.TimeData.NONE;
    private static int clientTimeAccelPhase = 0;

    public static void setClientData(UUID timeManipulatorUUID, WorldCap.TimeData timeData, int timeAccelPhase) {
        clientTimeManipulatorUUID = timeManipulatorUUID;
        clientTimeData = timeData;
        clientTimeAccelPhase = timeAccelPhase;
    }

    public static UUID getClientTimeManipulatorUUID() {
        return clientTimeManipulatorUUID;
    }

    public static void setClientTimeManipulatorUUID(UUID timeManipulatorUUID) {
        clientTimeManipulatorUUID = timeManipulatorUUID;
    }

    public static WorldCap.TimeData getClientTimeData() {
        return clientTimeData;
    }

    public static void setClientTimeData(WorldCap.TimeData timeData) {
        clientTimeData = timeData;
    }

    public static int getClientTimeAccelPhase() {
        return clientTimeAccelPhase;
    }

    public static void setClientTimeAccelPhase(int timeAccelPhase) {
        clientTimeAccelPhase = timeAccelPhase;
    }

    @Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents{
        private static final Minecraft mc = Minecraft.getInstance();
        private static boolean synced = false;

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;

            if (mc.player == null) {
                if (synced) {
                    setClientData(null, WorldCap.TimeData.NONE, 0);
                    synced = false;
                }
            } else {
                if (!synced) {
                    AddonPackets.sendToServer(new SyncClientWorldCapPacket(mc.player.getId()));
                    synced = true;
                }
            }
        }

//        @SubscribeEvent(priority = EventPriority.HIGHEST)
//        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//            System.out.println(mc.player);
//            System.out.println(event.getPlayer());
//            if (mc.player == event.getPlayer()) {
//                System.out.println(3);
//                AddonPackets.sendToServer(new SyncClientWorldCapPacket(mc.player.getId()));
//            }
//        }
//
//        @SubscribeEvent(priority = EventPriority.HIGHEST)
//        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//            if (mc.player == event.getPlayer()) {
//                System.out.println(1);
//                setClientData(null, WorldCap.TimeData.NONE, 0);
//            }
//        }
    }
}
