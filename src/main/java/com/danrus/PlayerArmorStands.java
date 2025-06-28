package com.danrus;

import com.danrus.config.ModConfig;
import com.danrus.render.PlayerArmorStandModel;
import com.danrus.utils.PASModelData;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.HashMap;

public class PlayerArmorStands implements ClientModInitializer {

    public static final String MOD_ID = "pas";
    public static final Gson GSON = new Gson();

    public static PlayerArmorStandModel model;

    public static HashMap <String, PASModelData> modelDataCache = new HashMap<>();

    @Override
    public void onInitializeClient() {
        PASCommandManager.register();
        ModConfig.initialize();
    }
}
