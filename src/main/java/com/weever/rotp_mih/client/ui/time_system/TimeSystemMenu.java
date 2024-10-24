package com.weever.rotp_mih.client.ui.time_system;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.InputHandler;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.github.standobyte.jojo.power.IPower;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Optional;

public class TimeSystemMenu extends Screen { // Weather Report && Kraft Work Reference
    private Optional<Type> currentlyHovered = Optional.empty();
    private static final int ALL_SLOTS_WIDTH = Type.values().length * 30 - 5;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    private static IPower.PowerClassification standPower = null;
    private IPower<?, ?> selectedPower;
    private final List<TimeSystemMenu.SelectorWidget> slots = Lists.newArrayList();
    private static final ITextComponent SELECT_TYPE = new TranslationTextComponent("rotp_mih.select_time_system").withStyle(TextFormatting.WHITE);
    private static final ITextComponent EMPTY = new TranslationTextComponent("empty", (new TranslationTextComponent("")));
    public static final ResourceLocation MENU = new ResourceLocation(MadeInHeavenAddon.MOD_ID, "textures/gui/time_system.png");

    public TimeSystemMenu() {
        super(NarratorChatListener.NO_TITLE);
    }

    public static void openWindowOnClick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            Screen screen = new TimeSystemMenu();
            mc.setScreen(screen);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputHandler.MouseButton button = InputHandler.MouseButton.getButtonFromId(pKeyCode);
        if (button == InputHandler.MouseButton.LEFT){
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    protected void init() {
        super.init();
        this.currentlyHovered = Type.getFromType(Type.CLEAR);
        for (int i = 0; i < Type.VALUES.length; ++i) {
            Type type = Type.VALUES[i];
            this.slots.add(new TimeSystemMenu.SelectorWidget(type, this.width / 2 - ALL_SLOTS_WIDTH / 2 + i * 30, this.height / 2 - 30));
        }
        if (standPower != null) {
            IPower.getPowerOptional(minecraft.player, standPower).ifPresent(power -> {
                if (!power.hasPower()) {
                    standPower = null;
                }
            });
        }
        for (IPower.PowerClassification powerClassification : IPower.PowerClassification.values()) {
            IPower.getPowerOptional(minecraft.player, powerClassification).ifPresent(power -> {
                if (power.hasPower()) {
                    if (selectedPower == null || powerClassification == ActionsOverlayGui.getInstance().getCurrentMode()) {
                        powersPresent(power);
                    }
                }
            });
        }

        if (standPower != null && selectedPower == null) {
            powersPresent(IPower.getPlayerPower(minecraft.player, standPower));
        }
    }

    private void powersPresent(IPower<?, ?> power) {
        if (power != null && power.hasPower()) {
            selectedPower = power;
            standPower = power.getPowerClassification();
        }
    }

    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(MENU);
        matrixStack.popPose();
        super.render(matrixStack, x, y, p_230430_4_);
        this.currentlyHovered.ifPresent((p_238712_2_) -> drawCenteredString(matrixStack, this.font, p_238712_2_.getName(), this.width / 2, this.height / 2 - 30 - 20, -1));
        drawCenteredString(matrixStack, this.font, SELECT_TYPE, this.width / 2, this.height / 2 + 5, 16777215);
        if (!this.setFirstMousePos) {
            this.firstMouseX = x;
            this.firstMouseY = y;
            this.setFirstMousePos = true;
        }

        boolean flag = this.firstMouseX == x && this.firstMouseY == y;

        for(TimeSystemMenu.SelectorWidget selectorWidget : this.slots) {
            selectorWidget.render(matrixStack, x, y, p_230430_4_);
            this.currentlyHovered.ifPresent((type) -> selectorWidget.setSelected(type == selectorWidget.icon));
            if (!flag && selectorWidget.isHovered()) {
                this.currentlyHovered = Optional.of(selectorWidget.icon);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (super.mouseClicked(mouseX, mouseY, buttonId)) {
            return true;
        }
        InputHandler.MouseButton button = InputHandler.MouseButton.getButtonFromId(buttonId);
        if (button == InputHandler.MouseButton.LEFT){
            switchToHoveredtimeSystemAndClose(this.minecraft, this.currentlyHovered);
        }
        return false;
    }

    private void switchToHoveredtimeSystemAndClose(Minecraft minecraft, Optional<Type> hovered) {
        if (hovered.isPresent()) {
            RayTraceResult target = InputHandler.getInstance().mouseTarget;
            ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
            StandEntityAction action;
            if (hovered.get() == Type.ACCELERATION) {
                action = InitStands.MIH_TIME_SYSTEM_ACCELERATION.get();
            } else {
                action = InitStands.MIH_TIME_SYSTEM_CLEAR.get();
            }
            ClClickActionPacket packet = new ClClickActionPacket(
                    standPower, action, actionTarget, false
            );
            PacketManager.sendToServer(packet);
            minecraft.setScreen(null);
        }
    }

    public enum Type {
        CLEAR(new TranslationTextComponent("timeSystem.clear"), "none"),
        ACCELERATION(new TranslationTextComponent("timeSystem.acceleration").withStyle(TextFormatting.LIGHT_PURPLE), "acceleration");

        public static final Type[] VALUES = values();
        final ITextComponent name;
        final String type;

        private void drawIcon(Type type, MatrixStack matrixStack, int x, int y) {
            Icon.renderVanillaIcon(type, matrixStack , x, y);
        }

        Type(ITextComponent name, String timeSystem){
            this.name = name;
            this.type = timeSystem;
        }

        public ITextComponent getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        private static Optional<Type> getFromType(Type type) {
            switch(type) {
                case CLEAR:
                    return Optional.of(CLEAR);
                case ACCELERATION:
                    return Optional.of(ACCELERATION);
                default:
                    return Optional.empty();
            }
        }
    }

    public class SelectorWidget extends Widget {
        private final Type icon;
        private boolean isSelected;
        public SelectorWidget(Type type, int x, int y) {
            super(x, y, 25, 25, EMPTY);
            this.icon = type;
        }
        public void renderButton(MatrixStack matrixStack, int x, int y, float p_230431_4_) {
            Minecraft minecraft = Minecraft.getInstance();
            this.drawSlot(matrixStack, minecraft.getTextureManager());
            this.icon.drawIcon(icon, matrixStack, this.x, this.y);
            if (this.isSelected) {
                this.drawSelection(matrixStack, minecraft.getTextureManager());
            }

        }

        public boolean isHovered() {
            return super.isHovered() || this.isSelected;
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
            this.narrate();
        }

        private void drawSlot(MatrixStack matrixStack, TextureManager textureManager) {
            textureManager.bind(MENU);
            matrixStack.pushPose();
            matrixStack.translate(this.x, this.y, 0.0D);
            blit(matrixStack, 0, 0, 0.0F, 0.0F, 25, 25, 128, 128);
            matrixStack.popPose();
        }

        private void drawSelection(MatrixStack matrixStack, TextureManager textureManager) {
            textureManager.bind(MENU);
            matrixStack.pushPose();
            matrixStack.translate(this.x, this.y, 0.0D);
            if (icon == Type.CLEAR) {
                blit(matrixStack, 0, 0, 100.0F, 0.0F, 25, 25, 128, 128);
            } else {
                blit(matrixStack, 0, 0, 25.0F, 0.0F, 25, 25, 128, 128);
            }
            matrixStack.popPose();
        }
    }
}

