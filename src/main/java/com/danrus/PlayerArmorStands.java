package com.danrus;

import com.danrus.config.ModConfig;
import com.danrus.utils.ASModelData;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import java.util.HashMap;

public class PlayerArmorStands implements ClientModInitializer {

    public static final String MOD_ID = "pas";
    public static final Gson GSON = new Gson();

    public static HashMap <String, ASModelData> modelDataCache = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ModConfig.initialize();
    }
}
