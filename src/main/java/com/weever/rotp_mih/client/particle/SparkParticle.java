package com.weever.rotp_mih.client.particle;


import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class SparkParticle extends SpriteTexturedParticle {

    protected SparkParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        lifetime = 20;
        quadSize = 0.15F;
        hasPhysics = true;
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
    }

    @Override
    public @NotNull IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public float getQuadSize(float partialTick) {
        float floatAge = (float) age + partialTick;
        float sizeFactor = floatAge < lifetime / 2F ? floatAge : lifetime - floatAge;
        return quadSize * MathHelper.clamp(sizeFactor / (lifetime / 2F) * 4F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            remove();
        }
    } // like a purple haze but no

//    @Override // CUM CODE🎉🎉🎉🎉
//    public void tick() {
//        this.quadSize *= 0.05f;
//
//        xo = x;
//        yo = y;
//        zo = z;
//
//        oRoll = roll;
//
//        move(xd, yd, zd);
//
//        if (this.age++ >= this.lifetime)
//            this.remove();
//    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}