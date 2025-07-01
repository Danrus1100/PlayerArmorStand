package com.danrus;

import com.danrus.config.ModConfig;
import com.danrus.render.PlayerArmorStandModel;
import com.danrus.utils.PASModelData;
import com.danrus.utils.skin.SkinsUtils;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class PlayerArmorStands implements ClientModInitializer {

    public static final String MOD_ID = "pas";
    public static final Gson GSON = new Gson();

    public static PlayerArmorStandModel model;
    //? if >=1.21.2
    /*public static PlayerArmorStandModel smallModel;*/

    public static HashMap <String, PASModelData> modelDataCache = new HashMap<>();

    @Override
    public void onInitializeClient() {
        //TODO перезагрузка модели на SHIFT + кнопка
        try {
            Files.createDirectories(SkinsUtils.CACHE_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PASCommandManager.register();
        ModConfig.initialize();
    }
}
