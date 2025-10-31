package com.danrus.pas.api;

import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.TextureUtils;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@Deprecated(forRemoval = true, since = "0.8.0")
public class LegacySkinData {
    public static ResourceLocation DEFAULT_TEXTURE = ModConfig.get().showArmorStandWhileDownloading
            ? Rl.vanilla("textures/entity/armorstand/wood.png")
            : Rl.vanilla("textures/entity/player/wide/steve.png");
    public static ResourceLocation DEFAULT_CAPE = Rl.pas("capes/cape.png");

    private String name;
    private List<TextureData> textures;
    private NameInfo info;
    private DownloadStatus status = DownloadStatus.NOT_STARTED;

    static class TextureData {
        String type;
        ResourceLocation texture;

        public TextureData(String type, ResourceLocation texture) {
            this.type = type;
            this.texture = texture;
        }
    }
//
//    public SkinData(String name, String params) {
//        this(name, DEFAULT_TEXTURE, DEFAULT_CAPE, params);
//    }

    public LegacySkinData(String name, ResourceLocation skinTexture, ResourceLocation capeTexture, NameInfo info) {
        this.name = name;
        this.info = info;
        this.textures = List.of(
                new TextureData("skin", skinTexture),
                new TextureData("cape", capeTexture)
        );
    }

//    public SkinData(String playerName, ResourceLocation skinTexture, ResourceLocation capeTexture, DownloadStatus status) {
//        this(playerName, skinTexture, capeTexture, "");
//        this.status = status;
//    }
//
//    public SkinData(String playerName, ResourceLocation skinTexture, ResourceLocation capeTexture) {
//        this(playerName, skinTexture, capeTexture, "");
//    }

//    public SkinData(String playerName) {
//        this(playerName, DEFAULT_TEXTURE, DEFAULT_CAPE, "");
//    }

    public LegacySkinData(NameInfo info) {
        this(info.base(), DEFAULT_TEXTURE, DEFAULT_CAPE, info);
    }

    public LegacySkinData(String name) {
        this(NameInfo.parse(name));
    }

    public LegacySkinData(NameInfo info, ResourceLocation skinTexture, ResourceLocation capeTexture) {
        this(info.base(), skinTexture, capeTexture, info);
    }

    public LegacySkinData(NameInfo info, ResourceLocation skinTexture) {
        this(info, skinTexture, DEFAULT_CAPE);
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTexture(String type) {
        if (type.isEmpty()) {
            return getDefaultTexture(type);
        }
        for (TextureData textureData : textures) {
            if (textureData.type.equals(type)) {
                return textureData.texture;
            }
        }
        return getDefaultTexture(type);
    }

    public ResourceLocation getDefaultTexture(String type) {
        return switch (type) {
            case "skin" -> DEFAULT_TEXTURE;
            case "cape" -> DEFAULT_CAPE;
            default -> MissingTextureAtlasSprite.getLocation();
        };
    }

    public ResourceLocation getSkinTexture(NameInfo info) {
        if (this.status != DownloadStatus.COMPLETED && this.status != DownloadStatus.NOT_STARTED) {
            return DEFAULT_TEXTURE;
        }
        if (!info.overlay().isEmpty()) {
            return TextureUtils.getOverlayTexture(info, info.overlay(), "skin", info.blend());
        }
        return getTexture("skin");
    }

    public ResourceLocation getCapeTexture(NameInfo info) {
        if (!info.overlay().isEmpty()) {
            return TextureUtils.getOverlayTexture(info, info.overlay(), "capes", info.blend());
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

    public NameInfo getNameInfo() {
        return info;
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
