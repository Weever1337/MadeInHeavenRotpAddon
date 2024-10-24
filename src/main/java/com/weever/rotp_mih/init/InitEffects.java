package com.weever.rotp_mih.init;

import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.effects.BleedingEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.POTIONS,
            MadeInHeavenAddon.MOD_ID
    );

    public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding",
            () -> new BleedingEffect(0xc7615a)
    );
}