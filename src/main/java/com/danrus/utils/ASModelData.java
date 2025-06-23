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
        ASModelData asModelData = new ASModelData(
                name,
                SkinsUtils.getSkinTexture(name),
                false,
                DownloadStatus.NOT_STARTED
        );
        return asModelData;
    }
}
