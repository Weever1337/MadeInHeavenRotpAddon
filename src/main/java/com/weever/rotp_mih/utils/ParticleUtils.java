package com.weever.rotp_mih.utils;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
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

    public static void createBallReverse(IParticleData particle, Vector3d vec, World world, int size, float speed) {
        if (!world.isClientSide())
            return;

        for (int i = -size; i <= size; ++i) {
            for (int j = -size; j <= size; ++j) {
                for (int k = -size; k <= size; ++k) {
                    double d3 = (double) j + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d4 = (double) i + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d5 = (double) k + (world.random.nextDouble() - world.random.nextDouble()) * 0.5D;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / speed + world.random.nextGaussian() * 0.05D;

                    double velocityX = d3 / d6;
                    double velocityY = -Math.abs(d4 / d6);
                    double velocityZ = d5 / d6;

                    world.addParticle(particle, vec.x(), vec.y(), vec.z(), velocityX, velocityY, velocityZ);

                    if (i != -size && i != size && j != -size && j != size)
                        k += size * 2 - 1;
                }
            }
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

    public static void createLineCommand(LivingEntity player, Vector3d start, Vector3d end, int amount) {
        Vector3d delta = end.subtract(start);
        Vector3d dir = delta.normalize();

        for (int i = 0; i < amount; ++i) {
            double progress = i * delta.length() / amount;
            double x = start.x + dir.x * progress;
            double y = start.y + dir.y * progress;
            double z = start.z + dir.z * progress;
            MCUtil.runCommand(
                    player,
                    "particle rotp_mih:cum "+x+" "+y+" "+z
                            //+ " " + motion.x + " " + motion.y + " " + motion.z
                    );
            //level.addParticle(particle, start.x + dir.x * progress, start.y + dir.y * progress,
                    //start.z + dir.z * progress, motion.x, motion.y, motion.z);
        }
    }
}