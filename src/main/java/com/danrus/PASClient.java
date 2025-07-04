package com.danrus;

import com.danrus.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PASClient implements ClientModInitializer {

    public static final String MOD_ID = "pas";
    public static final String MOD_NAME = "Player Armor Stands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        PASCommandManager.register();
        SkinManger.getInstance().init();
        ModConfig.initialize();
    }
}
