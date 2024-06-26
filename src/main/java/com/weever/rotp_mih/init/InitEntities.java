package com.weever.rotp_mih.init;

import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RotpMadeInHeavenAddon.MOD_ID);
}
