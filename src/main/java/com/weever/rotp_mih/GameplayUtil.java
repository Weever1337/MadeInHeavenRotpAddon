package com.weever.rotp_mih;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.brigadier.CommandDispatcher;
import com.weever.rotp_mih.command.CumCommand;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.server.RemoveTagPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.standobyte.jojo.action.stand.TimeResume.userTimeStopInstance;

@Mod.EventBusSubscriber(modid = RotpMadeInHeavenAddon.MOD_ID)
public class GameplayUtil {
    private static final int TICKS_FIRST_CLICK = TimeStopInstance.TIME_RESUME_SOUND_TICKS + 1;
    private static final Map<PlayerEntity, Integer> playerTickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        CumCommand.register(dispatcher);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player != null) {
            if (!player.level.isClientSide) {
                IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                    if(power.getType() != null) {
                        if (power.getType().getStats() instanceof TimeStopperStandStats) {
                            Optional<Entity> optionalMih = Optional.ofNullable(((ServerWorld) player.level).getEntities()
                                .filter(entity -> entity instanceof MihEntity)
                                .filter(entity -> ((MihEntity) entity).isTimeAccel())
                                .findFirst().orElse(null));
                            if (optionalMih.isPresent()) {
                                AtomicBoolean vampire = new AtomicBoolean(false);
                                INonStandPower.getNonStandPowerOptional(player).ifPresent(ipower -> {
                                    Optional<VampirismData> data = ipower.getTypeSpecificData(ModPowers.VAMPIRISM.get());
                                    if (data.isPresent()) {
                                        vampire.set(true);
                                    }
                                });
                                if (TimeStopHandler.isTimeStopped(player.level, player.blockPosition())) {
                                    playerTickCounters.putIfAbsent(player, 0);
                                    int tick = playerTickCounters.get(player) + 1;
                                    playerTickCounters.put(player, tick);

                                    if (tick >= 2 * ((TimeStopperStandStats) power.getType().getStats()).getMaxTimeStopTicks(vampire.get()) / 3) {
                                        userTimeStopInstance(player.level, player, instance -> {
                                            if (instance != null) {
                                                instance.setTicksLeft(!instance.wereTicksManuallySet() && instance.getTicksLeft() > TICKS_FIRST_CLICK ? TICKS_FIRST_CLICK : 0);
                                            }
                                        });
                                        playerTickCounters.put(player, 0);
                                    }
                                }
                            }
                        } else if(power.getType() == InitStands.MIH.getStandType()) {
                            if(!power.isActive() && player.getTags().contains("weever_heaven")){
                                player.removeTag("weever_heaven");
                                if(player instanceof ServerPlayerEntity){
                                    AddonPackets.sendToClient(new RemoveTagPacket(player.getId(),"weever_heaven"),(ServerPlayerEntity) player);
                                }
                            }
                        } else if (player.getTags().contains("weever_heaven")) {
                            player.removeTag("weever_heaven");
                            if(player instanceof ServerPlayerEntity){
                                AddonPackets.sendToClient(new RemoveTagPacket(player.getId(),"weever_heaven"),(ServerPlayerEntity) player);
                            }
                        }
                    }
                });
            }
        }
    }
}