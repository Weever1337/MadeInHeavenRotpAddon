package com.weever.rotp_mih.client.ui.time_system;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;


public class Icon {
    private static final ResourceLocation GUI = new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "textures/gui/time_system.png");

    public static void renderVanillaIcon(TimeSystemMenu.Type type, MatrixStack matrixStack, int x, int y) {
        Minecraft.getInstance().getTextureManager().bind(GUI);
        BlitFloat.blitFloat(matrixStack, x,y, getUByVanillaTimeSystem(type), getVByVanillaTimeSystem(type), 25, 25, 128, 128);
    }

    public static int getUByVanillaTimeSystem(TimeSystemMenu.Type type) { // x
        switch (type) {
            case CLEAR:
            case SLOW:
            case ACCELERATION:
                return 2;
            default:
                return 36;
        }
    }

    public static int getVByVanillaTimeSystem(TimeSystemMenu.Type type){ // y
        switch (type) {
            case CLEAR:
                return 48;
            case ACCELERATION:
                return 26;
            case SLOW:
                return 68;
            default:
                return 31;
        }
    }
}
