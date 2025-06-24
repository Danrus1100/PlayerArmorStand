package com.danrus.utils;

import com.danrus.PlayerArmorStands;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.util.Identifier;

import java.util.concurrent.ExecutionException;

public class ASModelData {
    public String name;
    public Identifier texture;
    public boolean slim;
    public DownloadStatus status = DownloadStatus.NOT_STARTED;

    public static Identifier DEFAULT_TEXTURE = Identifier.ofVanilla("textures/entity/player/wide/steve.png");

    public enum DownloadStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    public ASModelData(String name, Identifier texture, boolean slim) {
        this.name = name;
        this.texture = texture;
        this.slim = slim;
    }

    public ASModelData(String name, boolean slim, DownloadStatus status){
        this.name = name;
        this.texture = DEFAULT_TEXTURE;
        this.slim = slim;
        this.status = status;
    }

    public ASModelData(String name, Identifier texture, boolean slim, DownloadStatus status) {
        this.name = name;
        this.texture = texture;
        this.slim = slim;
        this.status = status;
    }

    public static ASModelData getByState(ArmorStandEntityRenderState state) throws ExecutionException, InterruptedException {
        String name = "steve";
        if (state.customName != null) {
            name = state.customName.getString();
            if (PlayerArmorStands.modelDataCache.containsKey(name)) {
                return PlayerArmorStands.modelDataCache.get(name);
            }
        }

        // Возвращаем базовую текстуру и асинхронно загружаем нужную
        Identifier defaultTexture = Identifier.ofVanilla("textures/entity/player/wide/steve.png");
        ASModelData defaultData = new ASModelData(name, defaultTexture, true, DownloadStatus.IN_PROGRESS);
        PlayerArmorStands.modelDataCache.put(name, defaultData);

        // Асинхронно загружаем настоящий скин
        SkinsUtils.getSkinTexture(name);

        return defaultData;
    }
}
