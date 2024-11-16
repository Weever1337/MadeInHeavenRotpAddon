package com.weever.rotp_mih.init;

import com.weever.rotp_mih.MadeInHeavenAddon;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MadeInHeavenAddon.MOD_ID);
}
