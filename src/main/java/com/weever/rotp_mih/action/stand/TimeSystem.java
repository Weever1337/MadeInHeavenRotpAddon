package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class TimeSystem extends StandEntityAction {
    public TimeSystem(Builder builder) {
        super(builder);
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
            if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION && TimeUtil.equalUUID(user.getUUID())) {
                WorldCapProvider.getWorldCap((ServerWorld) user.level).setTimeManipulatorUUID(null);
            } else if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.NONE) {
                WorldCapProvider.getWorldCap((ServerWorld) user.level).setTimeManipulatorUUID(user.getUUID());
                WorldCapProvider.getWorldCap((ServerWorld) user.level).setTimeData(WorldCap.TimeData.ACCELERATION);
            }
        }
    }

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        LivingEntity user = power.getUser();
        if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION && TimeUtil.equalUUID(user.getUUID())) {
            return new TranslationTextComponent(key + ".cast", new TranslationTextComponent("rotp_mih.time_system.clear"));
        } else {
            return new TranslationTextComponent(key + ".cast", new TranslationTextComponent("rotp_mih.time_system.acceleration"));
        }
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION && TimeUtil.equalUUID(power.getUser().getUUID());
    }

    private final LazySupplier<ResourceLocation> accelerationTex =
            new LazySupplier<>(() -> makeIconVariant(this, ".acceleration"));
    private final LazySupplier<ResourceLocation> clearTex =
            new LazySupplier<>(() -> makeIconVariant(this, ".clear"));

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        if (power != null) {
            if (TimeUtil.equalUUID(power.getUser().getUUID())) {
                return clearTex.get();
            } else {
                return accelerationTex.get();
            }
        }
        return accelerationTex.get();
    }
}