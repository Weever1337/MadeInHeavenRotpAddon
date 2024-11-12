package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCap.TimeData;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.entity.MadeInHeavenEntity;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.fromserver.ChangeMaxUpStepPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.standobyte.jojo.action.stand.TimeResume.userTimeStopInstance;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID)
public class GameplayEventHandler {
    private static final int TICKS_FIRST_CLICK = TimeStopInstance.TIME_RESUME_SOUND_TICKS + 1;
    public static final Map<LivingEntity, Integer> entityTickCounters = new HashMap<>();
    public static final UUID SPEED = UUID.fromString("0e9584f4-6936-41fc-8ddb-ab20a4ba626a");
    public static final UUID SWIM = UUID.fromString("c4c806cc-b788-4503-aa45-6f35cb03f1ba");
    public static List<LivingEntity> haveBoosts = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAccelerate(LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (!livingEntity.level.isClientSide()) {
            if (TimeUtil.equalUUID(livingEntity.getUUID())) {
                if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION) {
                    if (IStandPower.getStandPowerOptional(livingEntity).isPresent() && IStandPower.getStandPowerOptional(livingEntity).map(p -> p.getType() == InitStands.MADE_IN_HEAVEN.getStandType()).orElse(false)) {
                        int phase = WorldCapProvider.getClientTimeAccelPhase();
                        IStandPower power = IStandPower.getStandPowerOptional(livingEntity).orElse(null);
                        accelerateTime(livingEntity, phase, power);
                        boostOnAcceleration(livingEntity, (StandEntity) power.getStandManifestation(), phase);
                        if (!haveBoosts.contains(livingEntity)) {
                            haveBoosts.add(livingEntity);
                        }
                    }
                }
            } else {
                removeBoost(livingEntity);
            }
        }
    }

    private static void boostOnAcceleration(LivingEntity livingEntity, StandEntity standEntity, int timeAccelPhase) {
        ModifiableAttributeInstance speed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        ModifiableAttributeInstance swim = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (livingEntity.isSprinting() || (livingEntity.isSwimming() && livingEntity.isInWater())) {
            if (timeAccelPhase >= 4) {
                livingEntity.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                    cap.addAfterimages(3, 100);
                });
                standEntity.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                    cap.addAfterimages(3, 100);
                });
                if (livingEntity instanceof ServerPlayerEntity) {
                    AddonPackets.sendToClient(new ChangeMaxUpStepPacket(livingEntity.getId(), 1f + TimeUtil.getCalculatedPhase(timeAccelPhase) / 10), (ServerPlayerEntity) livingEntity);
                }
            }
            speed.removeModifier(SPEED);
            speed.addTransientModifier(new AttributeModifier(
                    SPEED, "Acceleration", 0.3 * timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
            swim.removeModifier(SWIM);
            swim.addTransientModifier(new AttributeModifier(
                    SWIM, "Swim", 0.3 * timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else {
            removeBoost(livingEntity);
        }
    }

    private static void removeBoost(LivingEntity livingEntity) {
        if (haveBoosts.contains(livingEntity)) {
            haveBoosts.remove(livingEntity);
            ModifiableAttributeInstance speed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
            ModifiableAttributeInstance swim = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
            if (livingEntity instanceof ServerPlayerEntity) {
                AddonPackets.sendToClient(new ChangeMaxUpStepPacket(livingEntity.getId(), .6f), (ServerPlayerEntity) livingEntity);
            }
            speed.removeModifier(SPEED);
            swim.removeModifier(SWIM);
        }
    }

    private static void accelerateTime(LivingEntity livingEntity, int timeAccelPhase, IStandPower power) {
        MadeInHeavenEntity stand = (MadeInHeavenEntity) power.getStandManifestation();
        if (power.isActive() && stand != null && !stand.isArmsOnlyMode()) {
            if (TimeStopHandler.isTimeStopped(livingEntity.level, livingEntity.blockPosition())) {
                return;
            }
            long multiplier = 0;
            if (WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).getTickCounter() % 2 == 0) {
                multiplier = 20L * timeAccelPhase;
            }
            if (WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).getTickCounter() % 150 == 0) { // TODO: 150 maybe?
                if (timeAccelPhase <= 30) {
                    timeAccelPhase++;
                } else {
                    useAction(InitStands.MIH_UNIVERSE_RESET.get(), power);
                    removeBoost(livingEntity);
                    WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).setTimeManipulatorUUID(null);
                }
            }
            WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).setTickCounter(WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).getTickCounter()+1);
            WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).setTimeAccelerationPhase(timeAccelPhase);
            ((ServerWorld) livingEntity.level).setDayTime(livingEntity.level.getDayTime() + multiplier);
            if (!livingEntity.getDeltaMovement().equals(Vector3d.ZERO)) {
                power.consumeStamina(livingEntity.getSpeed() * 15);
            }
        } else {
            WorldCapProvider.getWorldCap((ServerWorld) livingEntity.level).setTimeManipulatorUUID(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide() && event.world.dimension() == World.OVERWORLD) {
            if (event.phase == TickEvent.Phase.START) {
                WorldCapProvider.getWorldCap((ServerWorld) event.world).tick();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingUpdate(LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity == null || livingEntity.level.isClientSide()) return;

        IStandPower.getStandPowerOptional(livingEntity).ifPresent(power -> {
            if (power.getType() == null) return;

            AtomicBoolean cont = new AtomicBoolean(false);
            AtomicReference<TimeStop> timeStop = new AtomicReference<>();

            power.getAllUnlockedActions().forEach(action -> {
                if (action instanceof TimeStop) {
                    cont.set(true);
                    timeStop.set((TimeStop) action);
                }
            });

            if (WorldCapProvider.getClientTimeData() == TimeData.ACCELERATION) {
                if (TimeStopHandler.isTimeStopped(livingEntity.level, livingEntity.blockPosition())) {
                    entityTickCounters.putIfAbsent(livingEntity, 0);
                    int tick = entityTickCounters.get(livingEntity) + 1;
                    entityTickCounters.put(livingEntity, tick);

                    if (tick >= 2 * timeStop.get().getMaxTimeStopTicks(power) / TimeUtil.getCalculatedPhase(WorldCapProvider.getClientTimeAccelPhase())) {
                        userTimeStopInstance(livingEntity.level, livingEntity, instance -> {
                            if (instance != null) {
                                instance.setTicksLeft(!instance.wereTicksManuallySet() && instance.getTicksLeft() > TICKS_FIRST_CLICK ? TICKS_FIRST_CLICK : 0);
                            }
                        });
                        entityTickCounters.put(livingEntity, 0);
                    }
                }
            }
        });
    }

    private static void useAction(Action<IStandPower> action, IStandPower power, boolean sneak) {
        if (power == null || power.getType() == null || action == null) return;
        LivingEntity user = power.getUser();
        AtomicBoolean haveAction = new AtomicBoolean(false);
        power.getAllUnlockedActions().forEach(a -> {
            if (a == action && a.getCooldownTechnical(power) > 0) {
                haveAction.set(true);
            }
        });
        if (!haveAction.get()) return;

        if (user.level.isClientSide()) {
            ClClickActionPacket packet = new ClClickActionPacket(
                power.getPowerClassification(), action, ActionTarget.EMPTY, sneak
            );
            PacketManager.sendToServer(packet);
        } else {
            power.clickAction(action, sneak, ActionTarget.EMPTY, null);
        }
    }

    private static void useAction(Action<IStandPower> action, IStandPower power) {
        useAction(action, power, false);
    }

    @SuppressWarnings("unused")
    private static void useAction(Action<IStandPower> action, LivingEntity livingEntity, boolean sneak) {
        useAction(action, IStandPower.getStandPowerOptional(livingEntity).orElse(null), sneak);
    }
}