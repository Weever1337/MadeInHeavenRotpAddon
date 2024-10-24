package com.weever.rotp_mih.power.impl.stand.type;


import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;

import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class MadeInHeavenStandType <T extends StandStats> extends EntityStandType<T> {
    public static final UUID SPEED = UUID.fromString("0e9584f4-6936-41fc-8ddb-ab20a4ba626a");
    public static final UUID SWIM = UUID.fromString("c4c806cc-b788-4503-aa45-6f35cb03f1ba");

    int phase = 1;
    int tickCounter = 0;
    int tickSlowCounter = 0;

    public MadeInHeavenStandType(int color, ITextComponent partName,
                              StandAction[] attacks, StandAction[] abilities,
                              Class<T> statsClass, T defaultStats, @Nullable StandTypeOptionals additions) {
        super(color, partName, attacks, abilities, statsClass, defaultStats, additions);
    }

    protected MadeInHeavenStandType(AbstractBuilder<?, T> builder) {
        super(builder);
    }

    @Override
    public void tickUser(LivingEntity user, IStandPower power) {
        if (!user.level.isClientSide()) {
            if (user.getUUID().equals(TimeUtil.getGlobalValue().getOwner())) {
                ModifiableAttributeInstance speed = user.getAttribute(Attributes.MOVEMENT_SPEED);
                ModifiableAttributeInstance swim = user.getAttribute(ForgeMod.SWIM_SPEED.get());
                if (user.isSprinting() || (user.isSwimming() && user.isInWater())) {
                    user.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                        cap.addAfterimages(3, 100);
                    });
                    speed.removeModifier(SPEED);
                    speed.addTransientModifier(new AttributeModifier(
                            SPEED, "Acceleration", 0.1 * TimeUtil.timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    swim.removeModifier(SWIM);
                    swim.addTransientModifier(new AttributeModifier(
                            SWIM, "Swim", 0.1 * TimeUtil.timeAccelPhase, AttributeModifier.Operation.MULTIPLY_TOTAL));
                } else {
                    speed.removeModifier(SPEED);
                    swim.removeModifier(SWIM);
                }
            }
        }
        MihEntity stand = (MihEntity) power.getStandManifestation();
        if (power.isActive() && stand != null && !stand.isArmsOnlyMode() && !isValue(TimeUtil.Values.NONE)) {
            if (TimeStopHandler.isTimeStopped(user.level, user.blockPosition())) {
                return;
            }
            long multiplier = 0;
            if (tickCounter % 2 == 0) {
                multiplier = 20L * phase;
            }
            if (tickCounter % 5 == 0) {
                if (phase <= 30) {
                    phase++;
                } else {
                    if (user.level.isClientSide()) {
                        MadeInHeavenAddon.LOGGER.debug("[CLIENT] Universe Reset Action");
                        ClClickActionPacket packet = new ClClickActionPacket(
                                Objects.requireNonNull(power).getPowerClassification(), InitStands.MIH_UNIVERSE_RESET.get(), ActionTarget.EMPTY, false
                        );
                        PacketManager.sendToServer(packet);
                    } else if (!user.level.isClientSide()) {
                        MadeInHeavenAddon.LOGGER.debug("[SERVER] Universe Reset Action");
                        power.clickAction(InitStands.MIH_UNIVERSE_RESET.get(), false, ActionTarget.EMPTY, null);
                    }
                }
            }
            tickCounter++;
            TimeUtil.timeAccelPhase = phase;
            if (!user.level.isClientSide()) {
                ((ServerWorld) user.level).setDayTime(user.level.getDayTime() + multiplier);
            } else {
                ((ClientWorld) user.level).setDayTime(user.level.getDayTime() + multiplier);
            }
        } else {
            clearAll();
        }
        super.tickUser(user, power);
    }

    public void clearAll() {
        tickCounter = 0;
        tickSlowCounter = 0;
        phase = 1;
        TimeUtil.timeAccelPhase = 1;
    }

    public int getPhase() { return phase; }

    private static TimeUtil.Values value;
    public static boolean isValue(TimeUtil.Values val) {
        return Objects.equals(val, value);
    }
    public void setValue(TimeUtil.Values val) {
        value = val;
    }

    public static class Builder<T extends StandStats> extends AbstractBuilder<Builder<T>, T> {

        @Override
        protected Builder<T> getThis() {
            return this;
        }

        @Override
        public MadeInHeavenStandType<T> build() {
            return new MadeInHeavenStandType<>(this);
        }

    }
}
