package com.weever.rotp_mih.action.stand.gui;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.action.stand.CustomStandEntityAction;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TimeSystemReleaseSlow extends CustomStandEntityAction {
    public TimeSystemReleaseSlow(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) { return; }
        MihEntity mih = (MihEntity) standEntity;
        PlayerEntity player = (PlayerEntity) userPower.getUser();
        if (GameplayUtil.getGlobalValue().getValue() == GameplayUtil.Values.NONE || GameplayUtil.getGlobalValue().getPlayer() == player.getUUID()) {
            mih.setValue(GameplayUtil.Values.SLOW);
            GameplayUtil.setGlobalValue(player.getUUID(), GameplayUtil.Values.SLOW);
            System.out.println(GameplayUtil.getGlobalValue().getPlayer());
            System.out.println(GameplayUtil.getGlobalValue().getValue());
        } else {
            player.displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.already_time_manipulation"), true);
        }
    }
}
