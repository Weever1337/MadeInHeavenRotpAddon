package com.weever.rotp_mih.init;

import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpMadeInHeavenAddon.MOD_ID);

    // ======================================== STAND ========================================
    public static final Supplier<SoundEvent> MIH_SUMMON = SOUNDS.register("mih_summon",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_summon"))
    );

    public static final Supplier<SoundEvent> MIH_UNSUMMON = SOUNDS.register("mih_unsummon",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_unsummon"))
    );
    
    public static final Supplier<SoundEvent> MIH_BARRAGE = SOUNDS.register("mih_barrage",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_barrage"))
    );

    public static final Supplier<SoundEvent> MIH_IMPALE = SOUNDS.register("mih_impale",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_impale"))
    );

    public static final Supplier<SoundEvent> MIH_DASH = SOUNDS.register("mih_dash",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_dash"))
    );

    public static final Supplier<SoundEvent> MIH_TWO_STEPS = SOUNDS.register("mih_two_steps",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_two_steps"))
    );

    public static final Supplier<SoundEvent> MIH_TIME_ACCELERATION = SOUNDS.register("mih_time_acceleration",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_time_acceleration"))
    );

    public static final Supplier<SoundEvent> MIH_UNIVERSE_RESET = SOUNDS.register("mih_universe_reset",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_universe_reset"))
    );

    // ======================================== USER ========================================
    public static final Supplier<SoundEvent> MIH_SUMMON_USER = SOUNDS.register("mih_summon_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_summon_user"))
    );

    public static final Supplier<SoundEvent> MIH_BARRAGE_USER = SOUNDS.register("mih_barrage_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_barrage_user"))
    );

    public static final Supplier<SoundEvent> MIH_HEAVY_PUNCH_USER = SOUNDS.register("mih_heavy_punch_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_heavy_punch_user"))
    );

    public static final Supplier<SoundEvent> MIH_IMPALE_USER = SOUNDS.register("mih_impale_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_impale_user"))
    );

    public static final Supplier<SoundEvent> MIH_DASH_USER = SOUNDS.register("mih_dash_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_dash_user"))
    );

    public static final Supplier<SoundEvent> MIH_TIME_ACCELERATION_USER = SOUNDS.register("mih_time_acceleration_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_time_acceleration_user"))
    );

    public static final Supplier<SoundEvent> MIH_UNIVERSE_RESET_USER = SOUNDS.register("mih_universe_reset_user",
            () -> new SoundEvent(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_universe_reset_user"))
    );
    // ======================================== MISC ========================================
    static final OstSoundList MIH_OST = new OstSoundList(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "mih_ost"), SOUNDS);
}