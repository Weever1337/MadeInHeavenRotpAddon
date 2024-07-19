package com.weever.rotp_mih.entity.stand.stands;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.client.InputHandler;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitStands;
import net.lavabucket.hourglass.time.Time;
import net.lavabucket.hourglass.time.TimeService;
import net.lavabucket.hourglass.wrappers.ServerLevelWrapper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class MihEntity extends StandEntity {
	public MihEntity(StandEntityType<? extends StandEntity> type, World world) {
		super(type, world);
	}

	int phase = 1;
	int URphase = 1;
	int tickCounter = 0;
	int slowTick = 0;
	int tickSlowCounter = 0;

	private GameplayUtil.Values value;
	public boolean isValue(GameplayUtil.Values val) {
		return Objects.equals(val, this.value);
	}
	public void setValue(GameplayUtil.Values val) {
		this.value = val;
	}

	@Override
	public void tick() {
		super.tick();
		LivingEntity user = getUser();
		if (user != null) {
			if (isArmsOnlyMode() || this.getCurrentTaskAction() == ModStandsInit.UNSUMMON_STAND_ENTITY.get() && !isValue(GameplayUtil.Values.NONE)) {
				setValue(GameplayUtil.Values.NONE);
				return;
			}
			if (isValue(GameplayUtil.Values.ACCELERATION)) {
				clearSlowOnly();
				if (!level.isClientSide()) {
					long multiplier = 0;
					ServerLevelWrapper svw = new ServerLevelWrapper(this.level);
					TimeService timeService = new TimeService(svw);
					if (tickCounter % 2 == 0) {
						multiplier = 20L * phase;
					}
					if ((tickCounter + 1) % 75 == 0) {
						if (phase <= 15) {
							phase++;
						} else {
							if (GameplayUtil.getUniverseResetPlayer() != user) {
								GameplayUtil.setUniverseResetPlayer((PlayerEntity) user);
							}
						}
						if (URphase <= 30) {
							URphase++;
						} else {
							System.out.println("Universe Reset Action");
							RayTraceResult target = InputHandler.getInstance().mouseTarget;
							ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
							ClClickActionPacket packet = new ClClickActionPacket(
									Objects.requireNonNull(getUserPower()).getPowerClassification(), InitStands.MIH_UNIVERSE_RESET.get(), actionTarget, false
							);
							PacketManager.sendToServer(packet);
						}
					}
					tickCounter++;
					if (user.isSprinting() || (user.isSwimming() && user.isInWater())) {
						user.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> {
							cap.addAfterimages(3, 100);
						});
						user.addEffect(new EffectInstance(InitEffects.ACCELERATION.get(), 10, phase, false, false, true));
						user.addEffect(new EffectInstance(Effects.JUMP, 10, 1, false, false, true));
					}
					timeService.setDayTime(new Time(level.getDayTime(), multiplier));
				}
			} else if (isValue(GameplayUtil.Values.SLOW)) {
				clearAccelerationOnly();
				if (!level.isClientSide()) {
					if (slowTick == 0) {
						ServerLevelWrapper svw = new ServerLevelWrapper(this.level);
						slowTick = (int) svw.levelData.getDayTime();
					}
					((ServerWorld) level).setDayTime(slowTick);
					tickSlowCounter++;
					if (tickSlowCounter % 4 == 0) {
						slowTick++;
						slowTick++;
					}
				}
			} else {
				clearAll();
			}
		}
	}

	public void clearAll() {
		slowTick = 0;
		tickCounter = 0;
		tickSlowCounter = 0;
		phase = 1;
		URphase = 1;
	}

	public void clearAccelerationOnly() {
		tickCounter = 0;
		phase = 1;
		URphase = 1;
	}

	public void clearSlowOnly() {
		slowTick = 0;
		tickSlowCounter = 0;
	}
}