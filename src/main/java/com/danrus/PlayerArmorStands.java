package com.danrus;

import com.danrus.config.ModConfig;
import com.danrus.render.models.PlayerArmorStandModel;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class PlayerArmorStands implements ClientModInitializer {

    public static final String MOD_ID = "pas";

    @Override
    public void onInitializeClient() {
        //TODO перезагрузка модели на SHIFT + кнопка
        PASCommandManager.register();
        SkinsUtils.init();
        ModConfig.initialize();
    }
}
