package com.weever.rotp_mih.addoncompat.addon.expandability;

import com.weever.rotp_mih.utils.LiquidWalking;
import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExpandabilityEventHandler {
    @SubscribeEvent
    public void liquidWalking(LivingFluidCollisionEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            FluidState fluid = event.getFluidState();
            if (LiquidWalking.onLiquidWalkingEvent(entity, fluid)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
}
