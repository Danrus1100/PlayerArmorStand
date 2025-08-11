package com.danrus.pas.api;

import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SkinData {
    public static ResourceLocation DEFAULT_TEXTURE = VersioningUtils.getResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
    public static ResourceLocation DEFAULT_CAPE = VersioningUtils.getResourceLocation("pas","capes/cape.png");

    private String name;
    private List<TextureData> textures;
    private String params = "";
    private DownloadStatus status = DownloadStatus.NOT_STARTED;

    class TextureData {
        String type;
        ResourceLocation texture;

        public TextureData(String type, ResourceLocation texture) {
            this.type = type;
            this.texture = texture;
        }
    }

    public SkinData(String name, ResourceLocation skinTexture, ResourceLocation capeTexture, String params) {
        this.name = name;
        this.params = params.toUpperCase();
        this.textures = List.of(
                new TextureData("skin", skinTexture),
                new TextureData("cape", capeTexture)
        );
    }

    public SkinData(String playerName, ResourceLocation skinTexture, ResourceLocation capeTexture, DownloadStatus status) {
        this(playerName, skinTexture, capeTexture, "");
        this.status = status;
    }

    public SkinData(String playerName, ResourceLocation skinTexture, ResourceLocation capeTexture) {
        this(playerName, skinTexture, capeTexture, "");
    }

    public SkinData(String playerName) {
        this(playerName, DEFAULT_TEXTURE, DEFAULT_CAPE, "");
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTexture(String type) {
        for (TextureData textureData : textures) {
            if (textureData.type.equals(type)) {
                return textureData.texture;
            }
        }
        return switch (type) {
            case "skin" -> DEFAULT_TEXTURE;
            case "cape" -> DEFAULT_CAPE;
            default -> MissingTextureAtlasSprite.getLocation();
        };
    }

    public ResourceLocation getSkinTexture(String material, String blend) {
        if (material != null && !material.isEmpty()) {
            return TextureUtils.registerOverlayTexture(this.name, material, "skins", Integer.parseInt(blend));
        }
        return getTexture("skin");
    }

    public ResourceLocation getCapeTexture(String material, String blend) {
        if (material != null && !material.isEmpty()) {
            return TextureUtils.registerOverlayTexture(this.name, material, "capes", Integer.parseInt(blend));
        }
        return getTexture("cape");
    }

    public ResourceLocation getSkinTexture() {
        return getTexture("skin");
    }

    public ResourceLocation getCapeTexture() {
        return getTexture("cape");
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        if (status != DownloadStatus.IMPOSSIBLE_TO_DOWNLOAD){
            this.status = status;
        }
    }

    public void setSkinTexture(ResourceLocation skinTexture) {
        for (TextureData textureData : textures) {
            if (textureData.type.equals("skin")) {
                textureData.texture = skinTexture;
                return;
            }
        }
    }

    public void setCapeTexture(ResourceLocation capeTexture) {
        for (TextureData textureData : textures) {
            if (textureData.type.equals("cape")) {
                textureData.texture = capeTexture;
                return;
            }
        }
    }
}
