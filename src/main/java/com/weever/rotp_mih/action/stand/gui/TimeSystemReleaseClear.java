package com.weever.rotp_mih.action.stand.gui;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
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
        MihEntity mih = (MihEntity) standEntity;
        PlayerEntity player = (PlayerEntity) userPower.getUser();
        if (GameplayUtil.getGlobalValue().getValue() != GameplayUtil.Values.NONE && GameplayUtil.getGlobalValue().getPlayer() == player.getUUID()) {
            mih.setValue(GameplayUtil.Values.NONE);
            GameplayUtil.setGlobalValue(null, GameplayUtil.Values.NONE);
            System.out.println(GameplayUtil.getGlobalValue().getPlayer());
            System.out.println(GameplayUtil.getGlobalValue().getValue());
        } else {
            player.displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.already_cleared"), true);
        }
    }
}
