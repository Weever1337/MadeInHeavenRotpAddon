package com.weever.rotp_mih.mixin;

import com.weever.rotp_mih.capability.world.WorldCap;
import com.weever.rotp_mih.capability.world.WorldCapProvider;
import net.minecraft.block.*;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({CropsBlock.class, SaplingBlock.class, SweetBerryBushBlock.class, LeavesBlock.class, CactusBlock.class, NetherWartBlock.class})
public class GrowthMixin {
    @Inject(method = "randomTick", at = @At("HEAD"))
    public void onRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!world.isAreaLoaded(pos, 1)) return;
        if (WorldCapProvider.getClientTimeData() == WorldCap.TimeData.ACCELERATION) {
            if (world.getRawBrightness(pos, 0) >= 9) {
                if (state.getBlock() instanceof CropsBlock) {
                    CropsBlock crops = (CropsBlock) state.getBlock();
                    int age = state.getValue(crops.getAgeProperty());
                    int maxAge = crops.getMaxAge();
                    if (age < maxAge)
                        state.setValue(crops.getAgeProperty(), state.getValue(crops.getAgeProperty()) + 1);
                } else if (state.getBlock() instanceof SweetBerryBushBlock) {
                    IntegerProperty ageProperty = SweetBerryBushBlock.AGE;
                    int age = state.getValue(ageProperty);
                    if (age < 3)
                        state.setValue(SweetBerryBushBlock.AGE, state.getValue(SweetBerryBushBlock.AGE) + 1);
                } else if (state.getBlock() instanceof CactusBlock) {
                    state.setValue(CactusBlock.AGE, state.getValue(CactusBlock.AGE) + 1);
                } else if (state.getBlock() instanceof NetherWartBlock) {
                    IntegerProperty ageProperty = NetherWartBlock.AGE;
                    int age = state.getValue(ageProperty);
                    if (age < 3)
                        state.setValue(NetherWartBlock.AGE, state.getValue(NetherWartBlock.AGE) + 1);
                }
            }
        }
    }
}
