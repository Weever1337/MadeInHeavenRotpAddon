package com.weever.rotp_mih.init;

import com.github.standobyte.jojo.util.mc.OstSoundList;
import com.weever.rotp_mih.MadeInHeavenAddon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MadeInHeavenAddon.MOD_ID);

    // ======================================== STAND ========================================
    public static final Supplier<SoundEvent> MIH_SUMMON = SOUNDS.register("mih_summon",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_summon"))
    );

    public static final Supplier<SoundEvent> MIH_UNSUMMON = SOUNDS.register("mih_unsummon",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_unsummon"))
    );
    
    public static final Supplier<SoundEvent> MIH_BARRAGE = SOUNDS.register("mih_barrage",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_barrage"))
    );

    public static final Supplier<SoundEvent> MIH_CHOP = SOUNDS.register("mih_chop",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_chop"))
    );

    public static final Supplier<SoundEvent> MIH_DASH = SOUNDS.register("mih_dash",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_dash"))
    );

    public static final Supplier<SoundEvent> MIH_THROAT_SLICE = SOUNDS.register("mih_throat_slice",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_throat_slice"))
    );

    public static final Supplier<SoundEvent> MIH_TIME_ACCELERATION = SOUNDS.register("mih_time_acceleration",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_time_acceleration"))
    );

    // ======================================== USER ========================================
    public static final Supplier<SoundEvent> MIH_SUMMON_USER = SOUNDS.register("mih_summon_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_summon_user"))
    );

    public static final Supplier<SoundEvent> MIH_BARRAGE_USER = SOUNDS.register("mih_barrage_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_barrage_user"))
    );

    public static final Supplier<SoundEvent> MIH_HEAVY_PUNCH_USER = SOUNDS.register("mih_heavy_punch_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_heavy_punch_user"))
    );

    public static final Supplier<SoundEvent> MIH_CHOP_USER = SOUNDS.register("mih_chop_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_chop_user"))
    );

    public static final Supplier<SoundEvent> MIH_DASH_USER = SOUNDS.register("mih_dash_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_dash_user"))
    );

    public static final Supplier<SoundEvent> MIH_TIME_ACCELERATION_USER = SOUNDS.register("mih_time_acceleration_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_time_acceleration_user"))
    );

    public static final Supplier<SoundEvent> MIH_UNIVERSE_RESET_USER = SOUNDS.register("mih_universe_reset_user",
            () -> new SoundEvent(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_universe_reset_user"))
    );
    // ======================================== MISC ========================================
    static final OstSoundList MIH_OST = new OstSoundList(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "mih_ost"), SOUNDS);
}