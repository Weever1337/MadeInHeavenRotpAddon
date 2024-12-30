package com.weever.rotp_mih;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MadeInHeavenConfig {
    public static final Client CLIENT;
    static final ForgeConfigSpec clientSpec;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue isNewTSIconsEnabled;
        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client config");
            isNewTSIconsEnabled = builder.translation("rotp_mih.config.client.enableNewTSIcons")
                    .comment("  Determines if new clock related icons are enabled",
                            "  Default is to true.")
                    .define("enableNewTSIcons", true);
            builder.pop();
        }
    }
}
