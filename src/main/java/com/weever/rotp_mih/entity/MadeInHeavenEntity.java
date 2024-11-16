package com.weever.rotp_mih.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import net.minecraft.world.World;

public class MadeInHeavenEntity extends StandEntity {
	public MadeInHeavenEntity(StandEntityType<? extends StandEntity> type, World world) {
		super(type, world);
	}
}