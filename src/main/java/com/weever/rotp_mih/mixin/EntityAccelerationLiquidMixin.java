package com.weever.rotp_mih.mixin;

import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.utils.LiquidWalking;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@Mixin(Entity.class)
@SuppressWarnings("unused, had a lot of crashes. Use a HamonUtilMixin")
public class EntityAccelerationLiquidMixin {
    @ModifyVariable(method = "move", ordinal = 1, index = 3, name = "vec32", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;collide(Lnet/minecraft/util/math/vector/Vector3d;)Lnet/minecraft/util/math/vector/Vector3d;"))
    private Vector3d fluidCollision(Vector3d originalDisplacement) {
        if (!((Object) this instanceof LivingEntity)) {
            return originalDisplacement;
        }

        LivingEntity entity = (LivingEntity) (Object) this;

        if (originalDisplacement.y <= 0.0 && !isTouchingFluid(entity,entity.getBoundingBox().deflate(0.001D))) {
            Map<Vector3d, Double> points = findFluidDistances(entity, originalDisplacement);
            Double highestDistance = null;

            for (Map.Entry<Vector3d, Double> point : points.entrySet()) {
                if (highestDistance == null || (point.getValue() != null && point.getValue() > highestDistance)) {
                    highestDistance = point.getValue();
                }
            }

            if (highestDistance != null) {
                Vector3d finalDisplacement = new Vector3d(originalDisplacement.x, highestDistance, originalDisplacement.z);
                AxisAlignedBB finalBox = entity.getBoundingBox().move(finalDisplacement).deflate(0.001D);
                if (isTouchingFluid(entity, finalBox)) {
                    return originalDisplacement;
                } else {
                    entity.fallDistance = 0.0F;
                    entity.setOnGround(true);
                    return finalDisplacement;
                }
            }
        }

        return originalDisplacement;
    }

    @Unique
    private static Map<Vector3d, Double> findFluidDistances(LivingEntity entity, Vector3d originalDisplacement) {
        AxisAlignedBB box = entity.getBoundingBox().move(originalDisplacement);

        HashMap<Vector3d, Double> points = new HashMap<>();
        points.put(new Vector3d(box.minX, box.minY, box.minZ), null);
        points.put(new Vector3d(box.minX, box.minY, box.maxZ), null);
        points.put(new Vector3d(box.maxX, box.minY, box.minZ), null);
        points.put(new Vector3d(box.maxX, box.minY, box.maxZ), null);

        double fluidStepHeight = entity.isOnGround() ? Math.max(1.0, entity.maxUpStep) : 0.0;

        for (Map.Entry<Vector3d, Double> entry : points.entrySet()) {
            for (int i = 0; ; i--) {
                BlockPos landingPos = new BlockPos(entry.getKey()).offset(0.0, i + fluidStepHeight, 0.0);
                FluidState landingState = entity.getCommandSenderWorld().getFluidState(landingPos);

                double distanceToFluidSurface = landingPos.getY() + landingState.getOwnHeight() - entity.getY();
                double limitingVelocity = originalDisplacement.y;

                if (distanceToFluidSurface < limitingVelocity || distanceToFluidSurface > fluidStepHeight) {
                    break;
                }

                if (!landingState.isEmpty() && LiquidWalking.onLiquidWalkingEvent(entity, landingState)) {
                    entry.setValue(distanceToFluidSurface);
                    break;
                }
            }
        }

        return points;
    }

    @Unique
    private static boolean isTouchingFluid(LivingEntity entity, AxisAlignedBB box) {
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.ceil(box.maxX);
        int minY = MathHelper.floor(box.minY);
        int maxY = MathHelper.ceil(box.maxY);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.ceil(box.maxZ);
        World world = entity.getCommandSenderWorld();

        if (world.hasChunksAt(minX, minY, minZ, maxX, maxY, maxZ)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int i = minX; i < maxX; ++i) {
                for (int j = minY; j < maxY; ++j) {
                    for (int k = minZ; k < maxZ; ++k) {
                        mutable.set(i, j, k);
                        FluidState fluidState = world.getFluidState(mutable);

                        if (!fluidState.isEmpty()) {
                            double surfaceY = fluidState.getHeight(world, mutable) + j;

                            if (surfaceY >= box.minY) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
