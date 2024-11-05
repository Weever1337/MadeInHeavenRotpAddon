package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.entity.MadeInHeavenEntity;
import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.utils.ParticleUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
        if (!world.isClientSide) {
            LivingEntity user = userPower.getUser();
            if(user != null){
                world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(31), EntityPredicates.ENTITY_STILL_ALIVE).forEach(
                        livingEntity -> {
                            if (livingEntity == user || livingEntity instanceof MadeInHeavenEntity) return;
                            livingEntity.setDeltaMovement((x-livingEntity.getX())/10,(y-livingEntity.getY())/10,(z-livingEntity.getZ())/10);
                            livingEntity.hurt(DamageSource.playerAttack((PlayerEntity) userPower.getUser()), .5f);
                            livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 1, false, false, true));
                            livingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1, false, false, true));
                        }
                );
            }
        }
        if (world.isClientSide()) {
            ParticleUtils.createSpiral(InitParticles.SPARK.get(), world, new Vector3d(x, y, z), 5, 10);
        }
    }


    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()) {
            WorldCapProvider.getWorldCap((ServerWorld) world).setTimeManipulatorUUID(null);
        }
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        if(!world.isClientSide){
            LivingEntity User = standPower.getUser();
            if(User != null){
                x = User.getX();
                y = User.getY()+5;
                z = User.getZ();
            }
        }
    }
}