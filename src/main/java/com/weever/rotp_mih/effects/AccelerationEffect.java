package com.weever.rotp_mih.effects;

import com.github.standobyte.jojo.potion.IApplicableEffect;
import com.github.standobyte.jojo.potion.UncurableEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ForgeMod;

public class AccelerationEffect extends UncurableEffect implements IApplicableEffect {
    public AccelerationEffect(int color) {
        super(EffectType.BENEFICIAL, color);
        this.addAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        "0e9584f4-6936-41fc-8ddb-ab20a4ba626a",
                        0.4D,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
                .addAttributeModifier(
                        ForgeMod.SWIM_SPEED.get(),
                        "c4c806cc-b788-4503-aa45-6f35cb03f1ba",
                        0.4D,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                );
    }
    
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
	
	public boolean isApplicable(LivingEntity entity) {
        return true;
    }
}
