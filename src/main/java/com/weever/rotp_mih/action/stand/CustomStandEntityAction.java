package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;

public class CustomStandEntityAction extends StandEntityAction {
    public CustomStandEntityAction(Builder builder) {
        super(builder);
    }

    @Override
    public boolean canUserSeeInStoppedTime(LivingEntity user, IStandPower power) {
        return true;
    }
}
