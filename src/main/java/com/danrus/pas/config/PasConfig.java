package com.danrus.pas.config;


import com.danrus.pas.utils.ModUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class PasConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("[PAS Config]");

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
        if (ModUtils.isModLoaded("yet_another_config_lib_v3")) {
            return YaclConfig.get();
        }
        //?}
        return new DummyConfig();
    }

    public static void init() {
        //? if yacl {
        if (ModUtils.isModLoaded("yet_another_config_lib_v3")) {
            YaclConfig.init();
        }
        //?}
    }

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
