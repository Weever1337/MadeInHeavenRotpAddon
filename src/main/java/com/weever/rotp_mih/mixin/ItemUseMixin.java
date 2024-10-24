package com.weever.rotp_mih.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemUseMixin {
    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void onGetUseDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() / 2);
        System.out.println("Use duration: " + cir.getReturnValue());
    }
}

