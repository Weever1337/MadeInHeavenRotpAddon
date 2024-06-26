package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitEffects;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.network.AddonPackets;
import com.weever.rotp_mih.network.server.RemoveTagPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UniverseReset extends StandEntityAction {
    double x;
    double y;
    double z;
    public UniverseReset(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        super.checkStandConditions(stand, power, target);
        if (power.getStamina() < 300) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if(!world.isClientSide){
            LivingEntity user = userPower.getUser();
            if(user != null){
                MCUtil.runCommand(userPower.getUser(),"particle rotp_mih:cum "+x+" "+y+" "+z+" .5 .5 .5 1 30");
                world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(21), EntityPredicates.ENTITY_STILL_ALIVE).forEach(
                        livingEntity -> {
                            if (livingEntity == user || livingEntity instanceof MihEntity) return;
                            livingEntity.setDeltaMovement((x-livingEntity.getX())/10,(y-livingEntity.getY())/10,(z-livingEntity.getZ())/10);
                            livingEntity.addEffect(new EffectInstance(InitEffects.BLEEDING.get(), 100, 1, false, false, true));
                            livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 20, false, false, true));
                            livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 1, false, false, true));
                            livingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1, false, false, true));
                            if (livingEntity instanceof PlayerEntity && livingEntity != user)
                            IStandPower.getPlayerStandPower(((PlayerEntity) livingEntity)).setStamina(0);
                        }
                );
            }
        }
    }


    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        MihEntity MiH = (MihEntity) standEntity;
        MiH.setTimeAccel(false);
        LivingEntity User = userPower.getUser();
        User.removeTag("weever_heaven");
        if(User instanceof ServerPlayerEntity){
            AddonPackets.sendToClient(new RemoveTagPacket(User.getId(),"weever_heaven"),(ServerPlayerEntity) User);
        }
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        if(!world.isClientSide){
            LivingEntity User = standPower.getUser();
            if(User != null){
                x = User.getX();
                y = User.getY()+3;
                z = User.getZ();
            }
        }
    }
}