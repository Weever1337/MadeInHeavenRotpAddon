package com.weever.rotp_mih.action.stand.gui;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.power.impl.stand.type.MadeInHeavenStandType;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TimeSystemReleaseClear extends StandEntityAction {
    public TimeSystemReleaseClear(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) { return; }
        PlayerEntity player = (PlayerEntity) userPower.getUser();
        if (TimeUtil.getGlobalValue().getValue() != TimeUtil.Values.NONE && TimeUtil.getGlobalValue().getOwner() == player.getUUID()) {
            ((MadeInHeavenStandType<?>) userPower.getType()).setValue(TimeUtil.Values.NONE);
            TimeUtil.setGlobalValue(null, TimeUtil.Values.NONE);
            System.out.println(TimeUtil.getGlobalValue().getOwner());
            System.out.println(TimeUtil.getGlobalValue().getValue());
        } else {
            player.displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.already_cleared"), true);
        }
    }
}
