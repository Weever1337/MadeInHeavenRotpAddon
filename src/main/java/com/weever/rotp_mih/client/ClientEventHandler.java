package com.weever.rotp_mih.client;

import com.weever.rotp_mih.network.fromserver.ServerIdPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClientEventHandler {
    private static ClientEventHandler instance = null;
    private final Minecraft mc;

    private ClientEventHandler(Minecraft mc) {
        this.mc = mc;
    }

    public static void init(Minecraft mc) {
        if (instance == null) {
            instance = new ClientEventHandler(mc);
            MinecraftForge.EVENT_BUS.register(instance);
        }
    }

    public static ClientEventHandler getInstance() {
        return instance;
    }

    private UUID serverId;
    private boolean isLoggedIn = false;

    public void setServerId(ServerIdPacket packet) {
        serverId = packet.serverId;
    }

    @Nullable
    public UUID getServerId() {
        return isLoggedIn ? serverId : null;
    }

    @SubscribeEvent
    public void clientLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
        isLoggedIn = true;
    }

    @SubscribeEvent
    public void clientLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        isLoggedIn = false;
    }
}
