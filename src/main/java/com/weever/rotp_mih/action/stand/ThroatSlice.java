package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.client.ClientHandler;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
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

public class ThroatSlice extends StandEntityAction {
    private static final double RANGE = 3.0;
    private static final float DAMAGE = 3.0f;

    public ThroatSlice(Builder builder) {
        super(builder);
    }

    @Override
    public ActionConditionResult checkConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!TimeUtil.checkConditions(user, power, true))
            return ActionConditionResult.createNegative(new TranslationTextComponent("rotp_mih.message.action_condition.cant_use_without_acceleration"));
        if (TimeUtil.getTimeAccelPhase(user.level) < TimeUtil.GIVE_BUFFS) return ActionConditionResult.NEGATIVE;
        if (power.getStamina() < 50) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        if (!world.isClientSide()) {
            Vector3d startVec = user.position().add(0, user.getEyeHeight(), 0);
            Vector3d lookAt = customLookAt(world, user);

            List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(startVec, lookAt), EntityPredicates.ENTITY_STILL_ALIVE.and(e -> e != user && e != standEntity));
            targets.forEach(
                    target -> {
                        int duration = 150;
                        int amplifier = 2;

                        EffectInstance existingEffect = target.getEffect(ModStatusEffects.BLEEDING.get());
                        if (existingEffect != null) {
                            duration += existingEffect.getDuration();
                            amplifier = Math.min(existingEffect.getAmplifier() + 1, 6);
                        }
                        target.addEffect(new EffectInstance(ModStatusEffects.BLEEDING.get(), duration, amplifier, false, false, true));
                        target.hurt(DamageSource.playerAttack((PlayerEntity) userPower.getUser()), DAMAGE);
                    }
            );
            user.teleportTo(lookAt.x, lookAt.y, lookAt.z);
        }
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower userPower, Phase phase, StandEntityTask task, int ticks) {
        if (!world.isClientSide()) {
            world.playSound(null, standEntity.blockPosition(), InitSounds.MIH_THROAT_SLICE.get(), SoundCategory.VOICE, 1, 1);
        }
    }

    private static Vector3d customLookAt(World world, LivingEntity user) {
        Vector3d startVec = user.position().add(0, user.getEyeHeight(), 0);
        Vector3d lookVec = user.getLookAngle();
        Vector3d endVec = startVec.add(lookVec.scale(RANGE));

        RayTraceContext rayTraceContext = new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, user);
        BlockRayTraceResult blockRayTraceResult = world.clip(rayTraceContext);

        if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
            endVec = blockRayTraceResult.getLocation();
        }
        return endVec;
    }
}