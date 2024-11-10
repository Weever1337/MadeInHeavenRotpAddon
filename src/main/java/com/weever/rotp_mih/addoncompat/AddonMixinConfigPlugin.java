package com.weever.rotp_mih.addoncompat;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AddonMixinConfigPlugin implements IMixinConfigPlugin {
    private static final int PACKAGE_NAME_LENGTH = "com.weever.rotp_mih.mixin.".length();

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName.substring(PACKAGE_NAME_LENGTH)) {
            case "EntityAccelerationLiquidMixin":
                return !isModPresent("expandability");
            default:
                break;
        }
        return true;
    }

    private static boolean isModPresent(String modId) {
        LoadingModList loadingModList = FMLLoader.getLoadingModList();
        return loadingModList == null || loadingModList.getModFileById(modId) != null;
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
