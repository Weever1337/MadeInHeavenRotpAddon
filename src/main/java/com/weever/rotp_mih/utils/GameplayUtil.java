package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.WorldCap;
import com.weever.rotp_mih.capability.WorldCapProvider;
import com.weever.rotp_mih.entity.MadeInHeavenEntity;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID)
public class GameplayUtil {
    public static final UUID SPEED = UUID.fromString("0e9584f4-6936-41fc-8ddb-ab20a4ba626a");
    public static final UUID SWIM = UUID.fromString("c4c806cc-b788-4503-aa45-6f35cb03f1ba");

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerDeathWithTimeManipulation(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide() && player instanceof ServerPlayerEntity) {
                if (WorldCapProvider.getWorldCap((ServerPlayerEntity) player).getTimeData() != WorldCap.TimeData.NONE) {
                    if (WorldCapProvider.getWorldCap((ServerPlayerEntity) player).getTimeManipulatorUUID().equals(player.getUUID())) {
                        WorldCapProvider.getWorldCap((ServerPlayerEntity) player).setTimeManipulatorUUID(null);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.level.isClientSide() && player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            if (WorldCapProvider.getWorldCap((ServerPlayerEntity) player).getTimeManipulatorUUID() == player.getUUID()) {
                if (WorldCapProvider.getWorldCap(serverPlayer).getTimeData() == WorldCap.TimeData.ACCELERATION) {
                    if (IStandPower.getStandPowerOptional(player).isPresent() && IStandPower.getStandPowerOptional(player).map(p -> p.getType() == InitStands.MADE_IN_HEAVEN.getStandType()).orElse(false)) {
                        int phase = WorldCapProvider.getWorldCap(serverPlayer).getTimeAccelerationPhase();
                        IStandPower power = IStandPower.getStandPowerOptional(player).orElse(null);
                        boostOnAcceleration(player, phase);
                        accelerateTime(serverPlayer,phase, power);
                    }
                }
            }
        }
    }

    private static void boostOnAcceleration(PlayerEntity user, int timeAccelPhase) {
        ModifiableAttributeInstance speed = user.getAttribute(Attributes.MOVEMENT_SPEED);
        ModifiableAttributeInstance swim = user.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (user.isSprinting() || (user.isSwimming() && user.isInWater())) {
            user.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                cap.addAfterimages(3, 100);
            });
            speed.removeModifier(SPEED);
            speed.addTransientModifier(new AttributeModifier(
                    SPEED, "Acceleration", 0.1 * timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
            swim.removeModifier(SWIM);
            swim.addTransientModifier(new AttributeModifier(
                    SWIM, "Swim", 0.1 * timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else {
            speed.removeModifier(SPEED);
            swim.removeModifier(SWIM);
        }
    }

    private static void accelerateTime(ServerPlayerEntity player, int timeAccelPhase, IStandPower power) {
        MadeInHeavenEntity stand = (MadeInHeavenEntity) power.getStandManifestation();
        if (power.isActive() && stand != null && !stand.isArmsOnlyMode()) {
            if (TimeStopHandler.isTimeStopped(player.level, player.blockPosition())) {
                return;
            }
            long multiplier = 0;
            if (WorldCapProvider.getWorldCap(player).getTickCounter() % 2 == 0) {
                multiplier = 20L * timeAccelPhase;
            }
            if (WorldCapProvider.getWorldCap(player).getTickCounter() % 100 == 0) {
                if (timeAccelPhase <= 30) {
                    timeAccelPhase++;
                } else {
                    MadeInHeavenAddon.LOGGER.debug("[SERVER] Universe Reset Action");
                    ClClickActionPacket packet = new ClClickActionPacket(
                        power.getPowerClassification(), InitStands.MIH_UNIVERSE_RESET.get(), ActionTarget.EMPTY, false
                    );
                    PacketManager.sendToServer(packet);
                }
            }
            WorldCapProvider.getWorldCap(player).setTickCounter(WorldCapProvider.getWorldCap(player).getTickCounter()+1);
            WorldCapProvider.getWorldCap(player).setTimeAccelerationPhase(timeAccelPhase);
            ((ServerWorld) player.level).setDayTime(player.level.getDayTime() + multiplier);
        } else {
            WorldCapProvider.getWorldCap(player).setTimeManipulatorUUID(null);
            WorldCapProvider.getWorldCap(player).setTimeData(WorldCap.TimeData.NONE);
            WorldCapProvider.getWorldCap(player).setTimeAccelerationPhase(0);
            WorldCapProvider.getWorldCap(player).setTickCounter(0);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.dimension() == World.OVERWORLD) {
            if (event.phase == TickEvent.Phase.START) {
                WorldCapProvider.getWorldCap(Objects.requireNonNull(event.world.getServer())).tick();
            }
        }
    }
}