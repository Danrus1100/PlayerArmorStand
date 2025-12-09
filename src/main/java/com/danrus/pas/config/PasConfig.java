package com.danrus.pas.config;


import com.danrus.pas.utils.ModUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public abstract class PasConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("[PAS Config]");
    private static PasConfig instace;

    public void copyFields(PasConfig target) {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ConfigField.class)) {
                try {
                    Object value = field.get(this);
                    field.set(target, value);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Failed to copy config field: {}", field.getName(), e);
                }
            }
        }
    }

    public static PasConfig getInstance() {
        //? if yacl {
        if (ModUtils.isModLoaded(ModUtils.YACL_MOD_ID)) {
            return getInstance(YaclConfig::get);
        }
        //?}
        return getInstance(DummyConfig::new);
    }

    private static PasConfig getInstance(Supplier<PasConfig> factory) {
        if (instace == null) {
            instace = factory.get();
        }
        return instace;
    }

    public static void init() {
        //? if yacl {
        if (ModUtils.isModLoaded(ModUtils.YACL_MOD_ID)) {
            YaclConfig.initialize();
        }
        //?}
    }

    public abstract boolean is3dRpSetByMod();
    public abstract boolean isEnableMod();
    public abstract int getDownloadThreads();
    public abstract DownloadStatusDisplay getDownloadStatusDisplay();
    public abstract boolean isHideParamsOnLabel();
    public abstract String getDefaultSkin();
    public abstract boolean isShowArmorStandWhileDownloading();
    public abstract boolean isShowEasterEggs();
    public abstract boolean isTryApplyFromServerPlayer();
    public abstract boolean isPossessiveShowDefaultHand();
}
