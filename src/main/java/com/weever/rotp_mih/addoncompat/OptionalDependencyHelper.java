package com.weever.rotp_mih.addoncompat;

import com.weever.rotp_mih.MadeInHeavenAddon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class OptionalDependencyHelper {
    public static void init() {
        Object expandabilityEventHandler = createIfModPresent("expandability",
                "com.weever.rotp_mih.addoncompat.addon.expandability.ExpandabilityEventHandler",
                "Expandability Lib");
        if (expandabilityEventHandler != null) {
            MinecraftForge.EVENT_BUS.register(expandabilityEventHandler);
        }
    }

    public static Object createIfModPresent(String modId,
                                            String modPresentClassName,
                                            String loggingModName) {
        if (ModList.get().isLoaded(modId)) {
            try {
                Class<?> objClass = Class.forName(modPresentClassName);
                Constructor<?> constructor = objClass.getConstructor();
                Object instance = constructor.newInstance();
                MadeInHeavenAddon.LOGGER.debug("{}: {} compatibility initialized.", MadeInHeavenAddon.MOD_ID, loggingModName);
                return instance;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
            }
        }
        else {
            MadeInHeavenAddon.LOGGER.debug("{}: {} not found.", MadeInHeavenAddon.MOD_ID, loggingModName);
        }
        return null;
    }
}
