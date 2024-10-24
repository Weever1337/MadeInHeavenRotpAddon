package com.weever.rotp_mih.entity.stand.stands;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import net.minecraft.world.World;

public class MihEntity extends StandEntity {
	public MihEntity(StandEntityType<? extends StandEntity> type, World world) {
		super(type, world);
	}

//	int phase = 1;
//	int tickCounter = 0;
//	int slowTick = 0;
//	int tickSlowCounter = 0;
//
//	public int getPhase() { return phase; }
//
//	private GameplayUtil.Values value;
//	public boolean isValue(GameplayUtil.Values val) {
//		return Objects.equals(val, this.value);
//	}
//	public void setValue(GameplayUtil.Values val) {
//		this.value = val;
//	}
//
//	@Override
//	public void tick() {
//		super.tick();
//		LivingEntity user = getUser();
//		if (user != null) {
//			if (isArmsOnlyMode() || this.getCurrentTaskAction() == ModStandsInit.UNSUMMON_STAND_ENTITY.get() && !isValue(GameplayUtil.Values.NONE)) {
//				setValue(GameplayUtil.Values.NONE);
//				return;
//			}
//			if (TimeStopHandler.isTimeStopped(level, user.blockPosition())) {
//				return;
//			}
//			if (isValue(GameplayUtil.Values.ACCELERATION)) {
//				clearSlowOnly();
//				long multiplier = 0;
//				RayTraceResult rayTrace = JojoModUtil.rayTrace(user.getEyePosition(1.0F), user.getLookAngle(), 3,
//						level, user, e -> !(e.is(this) || e.is(user)), 0, 0);
//				ActionTarget target = ActionTarget.fromRayTraceResult(rayTrace);
//				if (tickCounter % 2 == 0) {
//					multiplier = 20L * phase;
//				}
//				if (tickCounter % 5 == 0) {
//					if (phase <= 30) {
//						phase++;
//					} else {
//						if (level.isClientSide()) {
//							MadeInHeavenAddon.LOGGER.debug("[CLIENT] Universe Reset Action");
//							ClClickActionPacket packet = new ClClickActionPacket(
//								Objects.requireNonNull(getUserPower()).getPowerClassification(), InitStands.MIH_UNIVERSE_RESET.get(), target, false
//							);
//							PacketManager.sendToServer(packet);
//						} else if (!level.isClientSide()) {
//							MadeInHeavenAddon.LOGGER.debug("[SERVER] Universe Reset Action");
//							IPower.getPowerOptional(user, Objects.requireNonNull(getUserPower()).getPowerClassification()).ifPresent(power -> {
//								clickAction(power, InitStands.MIH_UNIVERSE_RESET.get(), false, target);
//							});
//						}
//					}
//				}
//				tickCounter++;
//				GameplayUtil.timeAccelPhase = phase;
//				if (!level.isClientSide()) {
//					((ServerWorld) level).setDayTime(level.getDayTime() + multiplier);
//				}
//			} /*else if (isValue(GameplayUtil.Values.SLOW)) {
//				clearAccelerationOnly();
//				if (!level.isClientSide()) {
//					if (slowTick == 0) {
//						ServerLevelWrapper svw = new ServerLevelWrapper(this.level);
//						slowTick = (int) svw.levelData.getDayTime();
//					}
//					((ServerWorld) level).setDayTime(slowTick);
//					tickSlowCounter++;
//					if (tickSlowCounter % 4 == 0) {
//						slowTick++;
//						slowTick++;
//					}
//				}
//			}*/ else {
//				clearAll();
//			}
//		}
//	}
//
//	public void clearAll() {
//		slowTick = 0;
//		tickCounter = 0;
//		tickSlowCounter = 0;
//		phase = 1;
//		GameplayUtil.timeAccelPhase = 1;
//	}
//
//	private <P extends IPower<P, ?>> void clickAction(IPower<?, ?> power, Action<P> action, boolean sneak, ActionTarget target) {
//		((P) power).clickAction(action, sneak, target);
//	}
//
//	public void clearAccelerationOnly() {
//		tickCounter = 0;
//		phase = 1;
//		GameplayUtil.timeAccelPhase = 1;
//	}
//
//	public void clearSlowOnly() {
//		slowTick = 0;
//		tickSlowCounter = 0;
//	}
}