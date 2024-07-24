package com.weever.rotp_mih.mixin;

import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.standobyte.jojo.action.stand.TimeStop.getTimeStopTicks;

@Mixin(TimeStop.class)
public abstract class TimeStopMixin {
//    @Inject(method = "getMaxTimeStopTicks", at = @At("RETURN"), cancellable = true)
//    private static void onGetMaxTimeStopTicks(IStandPower standPower, CallbackInfoReturnable<Integer> cir) {
//        StandStats stats = standPower.getType().getStats();
//        LivingEntity user = standPower.getUser();
//        if (stats instanceof TimeStopperStandStats) {
//            TimeStopperStandStats timeStopperStandStats = (TimeStopperStandStats) stats;
//            if (GameplayUtil.getGlobalValue().getValue() == GameplayUtil.Values.ACCELERATION && GameplayUtil.getGlobalValue().getPlayer() != null) {
//                int phase = GameplayUtil.timeAccelPhase;
//                int delenie = 1;
//                switch (phase) {
//                    case 1: case 2: case 3: case 4: case 5:
//                        delenie = 2;
//                        break;
//                    case 6: case 7: case 8: case 9: case 10:
//                        delenie = 3;
//                        break;
//                    case 11: case 12: case 13: case 14: case 15:
//                        delenie = 4;
//                        break;
//                }
//                System.out.println(delenie);
//                cir.setReturnValue(setTimeStopTicks(user, delenie, timeStopperStandStats));
//            } else {
//                cir.setReturnValue(setTimeStopTicks(user, 1, timeStopperStandStats));
//                standPower.getAllUnlockedActions().forEach(ability -> {
//                    if (ability instanceof TimeStop) {
//                        GameplayUtil.playerTickCounters.put((PlayerEntity) user, getTimeStopTicks(standPower, ability));
//                    }
//                });
//            }
//        }
//    }
    // todo fix or rewrite or something like this please please please🤯🤯🤯

//    private static int setTimeStopTicks(LivingEntity user, int delenie, TimeStopperStandStats timeStopperStandStats) {
//        if (INonStandPower.getNonStandPowerOptional(user).isPresent() && INonStandPower.getPlayerNonStandPower((PlayerEntity) user).getType() == ModPowers.VAMPIRISM.get()) {
//            return timeStopperStandStats.getMaxTimeStopTicks(true) / delenie;
//        } else {
//            return timeStopperStandStats.getMaxTimeStopTicks(false) / delenie;
//        }
//    }

    @Inject(method = "canUserSeeInStoppedTime(Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/stand/IStandPower;)Z", at = @At("RETURN"), cancellable = true)
    private void onCanUserSeeInStoppedTime(LivingEntity user, IStandPower power, CallbackInfoReturnable<Boolean> cir) {
        if (GameplayUtil.getGlobalValue().getPlayer() == user.getUUID() && power.getType() == InitStands.MIH.getStandType()) {
            if (GameplayUtil.getGlobalValue().getValue() != GameplayUtil.Values.ACCELERATION) {
                cir.setReturnValue(false);
            } else {
                if (GameplayUtil.timeAccelPhase >= 3) {
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}