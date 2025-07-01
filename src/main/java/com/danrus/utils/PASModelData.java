package com.danrus.utils;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.skin.SkinsUtils;
import net.minecraft.util.Identifier;

public class PASModelData {
    public static Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
    public static Identifier DEFAULT_CAPE = Identifier.of("pas","capes/cape.png");

    public String name;
    public Identifier texture;
    public Identifier cape = DEFAULT_CAPE;
    public DownloadStatus status = DownloadStatus.NOT_STARTED;

    public enum DownloadStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        NOT_FOUND;

        public boolean isCompleted() {
            return this == COMPLETED;
        }

        public boolean inProgress() {
            return this == IN_PROGRESS;
        }

        public boolean isFailed() {
            return this == FAILED;
        }

        public boolean notFound() {
            return this == NOT_FOUND;
        }

        public boolean shouldDownload() {
            return this == NOT_STARTED || this == IN_PROGRESS;
        }
    }

    public PASModelData(String name) {
        this.name = name;
        this.texture = DEFAULT_TEXTURE;
    }

    public PASModelData(String name, Identifier texture) {
        this.name = name;
        this.texture = texture;
    }

    public PASModelData(String name, DownloadStatus status){
        this.name = name;
        this.texture = DEFAULT_TEXTURE;
        this.status = status;
    }

    public PASModelData(String name, Identifier texture, DownloadStatus status) {
        this.name = name;
        this.texture = texture;
        this.status = status;
    }

    public PASModelData(String name, Identifier texture, Identifier cape, DownloadStatus status) {
        this.name = name;
        this.texture = texture;
        this.status = status;
        this.cape = cape;
    }

    public PASModelData(String name, Identifier texture, Identifier cape) {
        this.name = name;
        this.texture = texture;
        this.cape = cape;
    }


    public static PASModelData getByName(String string) {

        String name = StringUtils.matchASName(string).get(0);
        boolean hasCape = StringUtils.matchASName(string).get(1).contains("C");

        if (hasCape) {
            if (PlayerArmorStands.modelDataCache.containsKey(name) && PlayerArmorStands.modelDataCache.get(name).cape == DEFAULT_CAPE) {
                Identifier capeId = Identifier.of("pas", "capes/" + StringUtils.encodeToSha256(name) + ".png");
                PASModelData.registerCape(name, capeId);
            }
        }

        if (!name.isEmpty() && PlayerArmorStands.modelDataCache.containsKey(name)) {
            return PlayerArmorStands.modelDataCache.get(name);
        }

        PASModelData defaultData = new PASModelData(name, DEFAULT_TEXTURE, DownloadStatus.IN_PROGRESS);
        register(defaultData);

        SkinsUtils.getSkinTexture(string);

        return defaultData;
    }

    private static void register(PASModelData data) {
            PlayerArmorStands.modelDataCache.put(data.name, data);
    }


    private static void register(String name, Identifier texture, DownloadStatus status) {
        register(new PASModelData(name, texture, status));
    }

    public static void registerFailed(String name) {
        register(name, DEFAULT_TEXTURE, DownloadStatus.FAILED);
    }

    public static void registerNotFound(String name) {
        register(name, DEFAULT_TEXTURE, DownloadStatus.NOT_FOUND);
    }

    public static void registerCompleted(String name, Identifier texture) {
        register(name, texture, DownloadStatus.COMPLETED);
    }

    public static void registerCape(String name, Identifier cape) {
        if (PlayerArmorStands.modelDataCache.containsKey(name)) {
            PASModelData data = PlayerArmorStands.modelDataCache.get(name);
            data.cape = cape;
            PlayerArmorStands.modelDataCache.put(name, data);
        } else {
            register(new PASModelData(name, DEFAULT_TEXTURE, cape, DownloadStatus.COMPLETED));
        }
    }
}
