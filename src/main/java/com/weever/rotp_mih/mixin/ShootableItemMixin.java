package com.weever.rotp_mih.mixin;

import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShootableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;

@Mixin(BowItem.class)
public abstract class ShootableItemMixin extends ShootableItem {
    public ShootableItemMixin(Properties p_i50040_1_) {
        super(p_i50040_1_);
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void onGetUseDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() / 2);
        System.out.println("Use duration: " + cir.getReturnValue());
    }
}

