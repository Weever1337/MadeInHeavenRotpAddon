package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.weever.rotp_mih.MadeInHeavenAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.standobyte.jojo.action.stand.TimeResume.userTimeStopInstance;
import static com.weever.rotp_mih.utils.TimeUtil.multiplyProjectileSpeed;

@Mod.EventBusSubscriber(modid = MadeInHeavenAddon.MOD_ID)
public class GameplayUtil {
    private static final int TICKS_FIRST_CLICK = TimeStopInstance.TIME_RESUME_SOUND_TICKS + 1;
    public static final Map<PlayerEntity, Integer> playerTickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) event.getEntity();
            if (!projectile.level.isClientSide()) {
                int multiply = TimeUtil.getCalculatedPhase();
                multiplyProjectileSpeed(projectile, multiply);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player != null && !player.level.isClientSide) {
            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                if (power.getType() != null && power.getType().getStats() instanceof TimeStopperStandStats) {
                    if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
                        AtomicBoolean vampire = new AtomicBoolean(false);
                        INonStandPower.getNonStandPowerOptional(player).ifPresent(ipower -> {
                            Optional<VampirismData> data = ipower.getTypeSpecificData(ModPowers.VAMPIRISM.get());
                            vampire.set(data.isPresent());
                        });
                        int phase = TimeUtil.timeAccelPhase;
                        int delenie = 1;
                        switch (phase) {
                            case 1: case 2: case 3: case 4: case 5:
                                delenie = 2;
                                break;
                            case 6: case 7: case 8: case 9: case 10:
                                delenie = 3;
                                break;
                            case 11: case 12: case 13: case 14: case 15:
                                delenie = 4;
                                break;
                        }
                        if (TimeStopHandler.isTimeStopped(player.level, player.blockPosition())) {
                            playerTickCounters.putIfAbsent(player, 0);
                            int tick = playerTickCounters.get(player) + 1;
                            playerTickCounters.put(player, tick);

                            if (tick >= 2 * ((TimeStopperStandStats) power.getType().getStats()).getMaxTimeStopTicks(vampire.get()) / delenie) {
                                userTimeStopInstance(player.level, player, instance -> {
                                    if (instance != null) {
                                        instance.setTicksLeft(!instance.wereTicksManuallySet() && instance.getTicksLeft() > TICKS_FIRST_CLICK ? TICKS_FIRST_CLICK : 0);
                                    }
                                });
                                playerTickCounters.put(player, 0);
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerDeathWithTimeManipulation(LivingDeathEvent event) {
        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.ACCELERATION) {
            if (event.getEntityLiving() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                if (TimeUtil.getGlobalValue().getOwner().equals(player.getUUID())) {
                    TimeUtil.setGlobalValue(null, TimeUtil.Values.NONE);
                }
            }
        }
    }
}