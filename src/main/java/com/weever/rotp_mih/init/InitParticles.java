package com.weever.rotp_mih.init;

import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RotpMadeInHeavenAddon.MOD_ID);

    public static final RegistryObject<BasicParticleType> SPARK = PARTICLES.register("spark", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> CUM = PARTICLES.register("cum", () -> new BasicParticleType(false));
}