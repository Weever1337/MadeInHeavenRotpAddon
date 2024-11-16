package com.weever.rotp_mih.utils;

import net.minecraft.block.BlockState;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class ParticleUtils {
    public static void createBall(IParticleData particle, Vector3d vec, World world, int size, float speed) {
        if (!world.isClientSide())
            return;

        for (int i = -size; i <= size; ++i) {
            for (int j = -size; j <= size; ++j) {
                for (int k = -size; k <= size; ++k) {
                    double d3 = (double) j + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d4 = (double) i + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d5 = (double) k + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / speed + world.random.nextGaussian() * 0.05D;

                    world.addParticle(particle, vec.x(), vec.y(), vec.z(),
                            d3 / d6, d4 / d6, d5 / d6);

                    if (i != -size && i != size && j != -size && j != size)
                        k += size * 2 - 1;
                }
            }
        }
    }

    public static void createCyl(IParticleData particle, Vector3d center, World level, double radius, float step) {
        int offset = 16;

        double len = (float) (2 * Math.PI * radius);
        int num = (int) (len / step);

        for (int i = 0; i < num; i++) {
            double angle = Math.toRadians(((360F / num) * i) + (360F * ((((len / step) - num) / num) / len)));

            double extraX = (radius * Math.sin(angle)) + center.x();
            double extraZ = (radius * Math.cos(angle)) + center.z();
            double extraY = center.y() + 0.5F;

            boolean foundPos = false;

            int tries;

            for (tries = 0; tries < offset * 2; tries++) {
                Vector3d vec = new Vector3d(extraX, extraY, extraZ);
                BlockPos pos = new BlockPos((int) vec.x, (int) vec.y, (int) vec.z);

                BlockState state = level.getBlockState(pos);
                VoxelShape shape = state.getCollisionShape(level, pos);

                if (shape.isEmpty()) {
                    if (!foundPos) {
                        extraY -= 1;

                        continue;
                    }
                } else
                    foundPos = true;

                if (shape.isEmpty())
                    break;

                AxisAlignedBB aabb = shape.bounds();

                if (!aabb.move(pos).contains(vec)) {
                    if (aabb.maxY >= 1F) {
                        extraY += 1;

                        continue;
                    }

                    break;
                }

                extraY += step;
            }

            if (tries < offset * 2)
                level.addParticle(particle, extraX, extraY + 0.1F, extraZ, 0, 0, 0);
        }
    }

    public static void createLine(IParticleData particle, World level, Vector3d start, Vector3d end, int amount, Vector3d motion) {
        Vector3d delta = end.subtract(start);
        Vector3d dir = delta.normalize();

        for (int i = 0; i < amount; ++i) {
            double progress = i * delta.length() / amount;

            level.addParticle(particle, start.x + dir.x * progress, start.y + dir.y * progress,
                    start.z + dir.z * progress, motion.x, motion.y, motion.z);
        }
    }

    public static void createLine(IParticleData particle, World level, Vector3d start, Vector3d end, int amount) {
        createLine(particle, level, start, end, amount, Vector3d.ZERO);
    }

//    public static void createNinjaHat(IParticleData particle, Vector3d center, World world, double radius, int loops, int particlesPerLoop, double height, double speed) {
//        if (!world.isClientSide())
//            return;
//
//        for (int loop = 0; loop < loops; loop++) {
//            double loopProgress = (double) loop / (double) loops;
//
//            for (int i = 0; i < particlesPerLoop; i++) { // btw, nevernight - good luck. I think, you understand, what I mean with comments below (because I forgot to remove after a "lesson" with math) =)
//                double angle = 2 * Math.PI * i / particlesPerLoop;
//                double spiralRadius = radius * (1 - loopProgress);
//                double x = center.x + spiralRadius * MathHelper.cos((float) angle);
//                double z = center.z + spiralRadius * MathHelper.sin((float) angle);
//                double y = center.y + height * loopProgress;
//
//                double motionX = -spiralRadius * MathHelper.cos((float) angle) * speed;
//                double motionY = -height * loopProgress * speed;
//                double motionZ = -spiralRadius * MathHelper.sin((float) angle) * speed;
//
//                world.addParticle(particle, x, y, z, motionX, motionY, motionZ);
//            }
//        }
//    }
}