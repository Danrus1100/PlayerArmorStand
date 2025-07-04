package com.danrus;

import com.danrus.enums.DownloadStatus;
import com.danrus.render.models.PASModel;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.util.Identifier;

import java.util.List;

public class PASModelData {
    public static Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
    public static Identifier DEFAULT_CAPE = Identifier.of("pas","capes/cape.png");

    private String name;
    private List<TextureData> textures;
    private String params = "";
    private DownloadStatus status = DownloadStatus.NOT_STARTED;
    private int downloadAttempts = 3;
    private PASModel model;

    class TextureData {
        String type;
        Identifier texture;

        public TextureData(String type, Identifier texture) {
            this.type = type;
            this.texture = texture;
        }
    }

    public PASModelData(String name, Identifier skinTexture, Identifier capeTexture, String params) {
        this.name = name;
        this.params = params.toUpperCase();
        this.textures = List.of(
                new TextureData("skin", skinTexture),
                new TextureData("cape", capeTexture)
        );
    }

    public PASModelData(String playerName, Identifier skinTexture, Identifier capeTexture) {
        this(playerName, skinTexture, capeTexture, "");
    }

    public PASModelData(String playerName) {
        this(playerName, DEFAULT_TEXTURE, DEFAULT_CAPE, "");
    }

    public String getName() {
        return name;
    }

    public Identifier getSkinTexture() {
        for (TextureData textureData : textures) {
            if (textureData.type.equals("skin")) {
                return textureData.texture;
            }
        }
        return DEFAULT_TEXTURE;
    }

    public Identifier getCapeTexture() {
        for (TextureData textureData : textures) {
            if (textureData.type.equals("cape")) {
                return textureData.texture;
            }
        }
        return DEFAULT_CAPE;
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
        for (TextureData textureData : textures) {
            if (textureData.type.equals("skin")) {
                textureData.texture = skinTexture;
                return;
            }
        }
    }

    public void setCapeTexture(Identifier capeTexture) {
        for (TextureData textureData : textures) {
            if (textureData.type.equals("cape")) {
                textureData.texture = capeTexture;
                return;
            }
        }
    }
}
