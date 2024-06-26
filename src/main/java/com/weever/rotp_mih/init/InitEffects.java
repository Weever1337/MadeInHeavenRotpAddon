package com.weever.rotp_mih.init;

import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.effects.*;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.POTIONS,
            RotpMadeInHeavenAddon.MOD_ID
    );
    public static final RegistryObject<Effect> ACCELERATION = EFFECTS.register("acceleration",
            () -> new AccelerationEffect(0x4b9ce3)
    );

    public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding",
            () -> new BleedingEffect(0xc7615a)
    );
}