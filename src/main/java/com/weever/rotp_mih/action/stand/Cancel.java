package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;

import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.server.RemoveTagPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class Cancel extends StandEntityAction {
    public Cancel(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) { return; }
        MihEntity MiH = (MihEntity) standEntity;
        LivingEntity User = (LivingEntity) userPower.getUser();
        MiH.setTimeAccel(false);
        User.removeTag("weever_heaven");
        if(User instanceof ServerPlayerEntity) {
            AddonPackets.sendToClient(new RemoveTagPacket(User.getId(),"weever_heaven"), (ServerPlayerEntity) User);
        }
    }
}