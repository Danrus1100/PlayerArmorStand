package com.danrus;

import com.danrus.enums.DownloadStatus;
import net.minecraft.util.Identifier;

public class PASModelData {
    public static Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
    public static Identifier DEFAULT_CAPE = Identifier.of("pas","capes/cape.png");

    private String name;
    private Identifier skinTexture;
    private Identifier capeTexture;
    private boolean slimModel;
    private DownloadStatus status = DownloadStatus.NOT_STARTED;

    public PASModelData(String name, Identifier skinTexture, Identifier capeTexture, boolean slimModel) {
        this.name = name;
        this.skinTexture = skinTexture;
        this.capeTexture = capeTexture;
        this.slimModel = slimModel;
    }

    public PASModelData(String playerName, Identifier skinTexture, Identifier capeTexture) {
        this(playerName, skinTexture, capeTexture, false);
    }

    public PASModelData(String playerName) {
        this(playerName, DEFAULT_TEXTURE, DEFAULT_CAPE, false);
    }

    public String getName() {
        return name;
    }

    public Identifier getSkinTexture() {
        return skinTexture;
    }

    public Identifier getCapeTexture() {
        return capeTexture;
    }

    public boolean isSlimModel() {
        return slimModel;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        if (status != DownloadStatus.IMPOSSIBLE_TO_DOWNLOAD){
            this.status = status;
        }
    }

    public void setSkinTexture(Identifier skinTexture) {
        this.skinTexture = skinTexture;
    }

    public void setCapeTexture(Identifier capeTexture) {
        this.capeTexture = capeTexture;
    }
}
