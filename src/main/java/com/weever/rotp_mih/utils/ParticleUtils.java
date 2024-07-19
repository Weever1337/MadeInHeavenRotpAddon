package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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

    public static void createLineCommand(LivingEntity player, Vector3d start, Vector3d end, int amount) {
        Vector3d delta = end.subtract(start);
        Vector3d dir = delta.normalize();

        for (int i = 0; i < amount; ++i) {
            double progress = i * delta.length() / amount;
            int x = (int) (start.x + dir.x * progress);
            int y = (int) (start.y + dir.y * progress);
            int z = (int) (start.z + dir.z * progress);

            MCUtil.runCommand(player, "particle rotp_mih:spark " + x + " " + y + " " + z + " 0 0 0 0 1 0");
//            level.addParticle(particle, start.x + dir.x * progress, start.y + dir.y * progress,
//                    start.z + dir.z * progress, motion.x, motion.y, motion.z);
        }
    }
}