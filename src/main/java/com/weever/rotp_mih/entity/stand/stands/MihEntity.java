package com.weever.rotp_mih.entity.stand.stands;

import com.github.standobyte.jojo.capability.entity.EntityUtilCapProvider;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.server.AddTagPacket;
import com.weever.rotp_mih.network.server.RemoveTagPacket;
import com.weever.rotp_mih.utils.ParticleUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.standobyte.jojo.action.stand.TimeResume.userTimeStopInstance;

public class MihEntity extends StandEntity {
	private static final DataParameter<Integer> DED_TICK = EntityDataManager.defineId(MihEntity.class, DataSerializers.INT);

	public MihEntity(StandEntityType<? extends StandEntity> type, World world) {
		super(type, world);
	}
	boolean timeAccel;

	public void setTimeAccel(boolean set) {
		this.timeAccel = set;
	}

	public boolean isTimeAccel() { return timeAccel; }

	int phase = 1;
	int tickCounter = 0;

	@Override
	public void tick() {
		super.tick();
		LivingEntity user = getUser();
		if(user != null && isTimeAccel()){
			long mult = 0;
			if (!this.isArmsOnlyMode()){
				if (tickCounter%4==0){
					mult = 20L * phase;
				}
				if ((tickCounter+1) %100==0){
					if(phase <10){
						phase++;
					} else {
						if(this.getUser() != null){
							if(!this.getUser().getTags().contains("weever_heaven")){
								this.getUser().addTag("weever_heaven");
								if (this.getUser() instanceof ServerPlayerEntity){
									AddonPackets.sendToClient(new AddTagPacket(this.getUser().getId(),"weever_heaven"),(ServerPlayerEntity) this.getUser());
								}
							}
						}
					}
				}
				tickCounter++;
				if (!level.isClientSide()) {
					user.addEffect(new EffectInstance(InitEffects.ACCELERATION.get(), 10, phase, false, false, true));
					user.addEffect(new EffectInstance(Effects.JUMP, 10, 1, false, false, true));
					((ServerWorld) level).setDayTime(level.getDayTime()+mult);
				}else {
					((ServerWorld) level).setDayTime(level.getDayTime()+mult);
				}
			} else {
				phase = 1;
				tickCounter = 0;
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DED_TICK, 0);
	}
}