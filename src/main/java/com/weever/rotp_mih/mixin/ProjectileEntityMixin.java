package com.weever.rotp_mih.mixin;

import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.weever.rotp_mih.utils.TimeUtil.multiplyProjectileSpeed;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {
    public ProjectileEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (this.getEntity().tickCount % 20 == 0) {
            if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION) {
                multiplyProjectileSpeed((ProjectileEntity) this.getEntity(), WorldCapProvider.getClientTimeAccelPhase() / 2);
            }
        }
    }
}
