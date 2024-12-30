package com.weever.rotp_mih.action.stand;

import org.jetbrains.annotations.Nullable;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.config.ActionConfigField;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.weever.rotp_mih.MadeInHeavenConfig;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.init.InitSounds;
import com.weever.rotp_mih.utils.TimeUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SStopSoundPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TimeSystem extends StandEntityAction {
	@ActionConfigField public final boolean canSeeInTimestopWhileAcceleration;
	
    public TimeSystem(Builder builder) {
        super(builder);
        this.canSeeInTimestopWhileAcceleration = true;
    }

    @Override
    public ActionConditionResult checkConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!TimeUtil.checkConditions(user, power, false))
            return ActionConditionResult.createNegative(new TranslationTextComponent("rotp_mih.message.action_condition.already_time_manipulation"));
        if (power.getStamina() < 300) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()) {
            LivingEntity user = userPower.getUser();
            WorldCap worldCap = WorldCapProvider.getWorldCap((ServerWorld) user.level);
            if (worldCap.getTimeData() == WorldCap.TimeData.ACCELERATION && TimeUtil.customEqualUUID(user.getUUID(), world)) {
                worldCap.setTimeManipulatorUUID(null, false);

                world.players().forEach(player -> {
                    stopSound(player, InitSounds.MIH_TIME_ACCELERATION.get(), SoundCategory.VOICE);
                    stopSound(player, InitSounds.MIH_TIME_ACCELERATION_USER.get(), SoundCategory.PLAYERS);
                });
            } else if (worldCap.getTimeData() == WorldCap.TimeData.NONE) {
                boolean isTimeManipulatorPlayer = user instanceof PlayerEntity;

                worldCap.setTimeManipulatorUUID(user.getUUID(), isTimeManipulatorPlayer);
                worldCap.setTimeData(WorldCap.TimeData.ACCELERATION);

                world.playSound(null, standEntity.blockPosition(), InitSounds.MIH_TIME_ACCELERATION.get(), SoundCategory.VOICE, 1, 1);
                world.playSound(null, standEntity.blockPosition(), InitSounds.MIH_TIME_ACCELERATION_USER.get(), SoundCategory.PLAYERS, 1, 1);

                userPower.setCooldownTimer(this, 10);
            }
        }
    }

    private static void stopSound(PlayerEntity player, SoundEvent sound, SoundCategory soundCategory) {
        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).connection.send(new SStopSoundPacket(sound.getRegistryName(), soundCategory));
        }
    }

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        LivingEntity user = power.getUser();
        if (TimeUtil.getTimeData(user.level) == WorldCap.TimeData.ACCELERATION && TimeUtil.customEqualUUID(user.getUUID(), null)) {
            return new TranslationTextComponent(key + ".cast", new TranslationTextComponent("rotp_mih.time_system.clear"));
        } else {
            return new TranslationTextComponent(key + ".cast", new TranslationTextComponent("rotp_mih.time_system.acceleration"));
        }
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return TimeUtil.getTimeData(power.getUser().level) == WorldCap.TimeData.ACCELERATION && TimeUtil.customEqualUUID(power.getUser().getUUID(), null);
    }

    private final LazySupplier<ResourceLocation> oldAccelerationTex =
            new LazySupplier<>(() -> makeIcon(this, true,".acceleration"));
    private final LazySupplier<ResourceLocation> oldClearTex =
            new LazySupplier<>(() -> makeIcon(this,  true, ".clear"));
    private final LazySupplier<ResourceLocation> newAccelerationTex =
            new LazySupplier<>(() -> makeIcon(this, false,".acceleration"));
    private final LazySupplier<ResourceLocation> newClearTex =
            new LazySupplier<>(() -> makeIcon(this,  false, ".clear"));

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        boolean isNewIcons = MadeInHeavenConfig.CLIENT.isNewTSIconsEnabled.get();
        boolean isTimeAccelerationActive = power != null &&
                TimeUtil.getTimeData(power.getUser().level) == WorldCap.TimeData.ACCELERATION &&
                TimeUtil.customEqualUUID(power.getUser().getUUID(), null);

        LazySupplier<ResourceLocation> selectedTexture = isTimeAccelerationActive
                ? (isNewIcons ? newClearTex : oldClearTex)
                : (isNewIcons ? newAccelerationTex : oldAccelerationTex);

        return selectedTexture.get();
    }

    private static ResourceLocation makeIcon(Action<?> action, boolean old, @Nullable String postfix) {
        String folder = old ? "old/" : "new/";
        String path = "time_system/" + folder + action.getRegistryName().getPath() + (postfix != null ? postfix : "");
        return JojoModUtil.makeTextureLocation("action", action.getRegistryName().getNamespace(), path);
    }
}