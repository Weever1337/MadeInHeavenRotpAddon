package com.weever.rotp_mih.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityBlock;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.StandEntityLightAttack;
import com.github.standobyte.jojo.action.stand.StandEntityMeleeBarrage;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.StoryPart;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.action.stand.Chop;
import com.weever.rotp_mih.action.stand.LightSpeedDash;
import com.weever.rotp_mih.action.stand.TimeSystem;
import com.weever.rotp_mih.action.stand.UniverseReset;
import com.weever.rotp_mih.entity.MadeInHeavenEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), MadeInHeavenAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), MadeInHeavenAddon.MOD_ID);

    // ======================================== Made In Heaven ========================================

    public static final RegistryObject<StandEntityAction> MIH_PUNCH = ACTIONS.register("mih_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder().cooldown(3)));

    public static final RegistryObject<StandEntityAction> MIH_BARRAGE = ACTIONS.register("mih_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .standSound(InitSounds.MIH_BARRAGE)
                    .shout(InitSounds.MIH_BARRAGE_USER))
    );

    public static final RegistryObject<StandEntityHeavyAttack> MIH_CHOP = ACTIONS.register("mih_chop",
            () -> new Chop(new StandEntityHeavyAttack.Builder()
                    .shout(InitSounds.MIH_CHOP_USER)
                    .partsRequired(StandPart.ARMS)
                    .cooldown(25))
    );

    public static final RegistryObject<StandEntityHeavyAttack> MIH_HEAVY_PUNCH = ACTIONS.register("mih_heavy_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder()
                    .partsRequired(StandPart.ARMS)
                    .setFinisherVariation(MIH_CHOP)
                    .shout(InitSounds.MIH_HEAVY_PUNCH_USER)
                    .shiftVariationOf(MIH_PUNCH).shiftVariationOf(MIH_BARRAGE))
    );

//    public static final RegistryObject<StandEntityAction> MIH_THROAT_SLICE = ACTIONS.register("mih_throat_slice",
//            () -> new ThroatSlice(new StandEntityAction.Builder()
//                    .partsRequired(StandPart.ARMS)
//                    .staminaCost(150).cooldown(160, 160).resolveLevelToUnlock(1)
//            )
//    );

    public static final RegistryObject<StandEntityAction> MIH_DASH = ACTIONS.register("mih_dash",
            () -> new LightSpeedDash(new StandEntityAction.Builder()
                    .standAutoSummonMode(StandEntityAction.AutoSummonMode.OFF_ARM)
                    //.shout(InitSounds.MIH_DASH_USER)
                    //.standSound(InitSounds.MIH_DASH)
                    .staminaCost(150).cooldown(50, 50).resolveLevelToUnlock(1)
                    .partsRequired(StandPart.LEGS)
                    .holdType(5))
    );

    public static final RegistryObject<StandEntityAction> MIH_BLOCK = ACTIONS.register("mih_block",
            () -> new StandEntityBlock());

    public static final RegistryObject<StandEntityAction> MIH_TIME_SYSTEM = ACTIONS.register("mih_time_system",
            () -> new TimeSystem(new StandEntityAction.Builder()
                    .autoSummonStand()
                    .holdToFire(25, false)
                    .partsRequired(StandPart.MAIN_BODY))
    );

    public static final RegistryObject<StandEntityAction> MIH_UNIVERSE_RESET = ACTIONS.register("mih_universe_reset",
            () -> new UniverseReset(new StandEntityAction.Builder()
                    .staminaCost(500).resolveLevelToUnlock(4)
                    .partsRequired(StandPart.MAIN_BODY)
                    .shout(InitSounds.MIH_UNIVERSE_RESET_USER))
    );

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<MadeInHeavenEntity>> MADE_IN_HEAVEN =
            new EntityStandRegistryObject<>("madeinheaven",
                    STANDS,
                    () -> new EntityStandType.Builder<>()
                            .color(0xD3D5E1)
                            .storyPartName(StoryPart.STONE_OCEAN.getName())
                            .leftClickHotbar(
                                    MIH_PUNCH.get(),
                                    MIH_BARRAGE.get()
//                                    MIH_THROAT_SLICE.get()
                            )
                            .rightClickHotbar(
                                    MIH_BLOCK.get(),
                                    MIH_DASH.get(),
                                    MIH_TIME_SYSTEM.get()
                            )
//                            .defaultKey(
//                                    MIH_THROAT_SLICE.get(),
//                                    "key.keyboard.x"
//                            )
                            .defaultKey(
                                    MIH_DASH.get(),
                                    "key.keyboard.c"
                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .power(13.0)
                                    .speed(20.0)
                                    .range(8, 10)
                                    .durability(14.0)
                                    .precision(10.0)
                                    .randomWeight(0.1)
                            )
                            .addOst(InitSounds.MIH_OST)
                            .addSummonShout(InitSounds.MIH_SUMMON_USER)
                            .build(),

                    InitEntities.ENTITIES,
                    () -> new StandEntityType<>(MadeInHeavenEntity::new, 0.7F, 1.8F)
                            .summonSound(InitSounds.MIH_SUMMON)
                            .unsummonSound(InitSounds.MIH_UNSUMMON)
                    ).withDefaultStandAttributes();

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<MadeInHeavenEntity>> MIH = MADE_IN_HEAVEN;
}