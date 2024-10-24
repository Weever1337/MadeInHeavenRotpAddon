package com.weever.rotp_mih.action.stand.gui;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.power.impl.stand.type.MadeInHeavenStandType;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TimeSystemReleaseAcceleration extends StandEntityAction {
    public TimeSystemReleaseAcceleration(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) { return; }
        PlayerEntity player = (PlayerEntity) userPower.getUser();
        if (TimeUtil.getGlobalValue().getValue() == TimeUtil.Values.NONE || TimeUtil.getGlobalValue().getOwner() == player.getUUID()) {
            ((MadeInHeavenStandType<?>) userPower.getType()).setValue(TimeUtil.Values.ACCELERATION);
            TimeUtil.setGlobalValue(player.getUUID(), TimeUtil.Values.ACCELERATION);
            System.out.println(TimeUtil.getGlobalValue().getOwner());
            System.out.println(TimeUtil.getGlobalValue().getValue());
            world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_TIME_ACCELERATION.get(), SoundCategory.PLAYERS,1,1);
        } else {
            player.displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.already_time_manipulation"), true);
        }
    }
}
