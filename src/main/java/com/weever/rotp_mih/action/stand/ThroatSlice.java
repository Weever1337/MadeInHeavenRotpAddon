package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.weever.rotp_mih.init.InitParticles;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.utils.ParticleUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ThroatSlice extends CustomStandEntityAction {
    private static final double RANGE = 7.0;
    private static final float DAMAGE = 5.0f;

    public ThroatSlice(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            MihEntity mihEntity = (MihEntity) standEntity;
            if (!mihEntity.isValue(GameplayUtil.Values.ACCELERATION)) {
                ((PlayerEntity) user).displayClientMessage(new TranslationTextComponent("rotp_mih.message.action_condition.cant_use_without_timeaccel"), true);
                return;
            }
            Vector3d startVec = user.position().add(0, user.getEyeHeight(), 0);
            Vector3d lookVec = user.getLookAngle();
            Vector3d endVec = startVec.add(lookVec.scale(RANGE));

            RayTraceContext rayTraceContext = new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, user);
            BlockRayTraceResult blockRayTraceResult = world.clip(rayTraceContext);

            if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
                endVec = blockRayTraceResult.getLocation();
            }

            List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(startVec, endVec), EntityPredicates.ENTITY_STILL_ALIVE.and(e -> e != user && e != standEntity));
            targets.forEach(
                    target -> target.hurt(DamageSource.playerAttack((PlayerEntity) userPower.getUser()), DAMAGE)
            );
            user.teleportTo(endVec.x, endVec.y, endVec.z);
        }
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower userPower, Phase phase, StandEntityTask task, int ticks) {
        MihEntity MiH = (MihEntity) standEntity;
        if (!world.isClientSide()) {
            if (MiH.isValue(GameplayUtil.Values.ACCELERATION)) {
                world.playSound(null,standEntity.blockPosition(), InitSounds.MIH_THROAT_SLICE.get(), SoundCategory.PLAYERS,1,1);
            }
        }
    }
}