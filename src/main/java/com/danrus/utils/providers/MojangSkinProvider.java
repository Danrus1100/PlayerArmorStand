package com.danrus.utils.providers;

import com.danrus.*;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.SkinProvider;
import com.danrus.utils.PASSkinDownloader;
import com.danrus.utils.RestHelper;
import com.danrus.utils.StringUtils;
import com.danrus.utils.data.MojangDiskCache;
import com.google.gson.Gson;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class MojangSkinProvider implements SkinProvider {

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SESSION_SERVER_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";

    private final Gson gson = new Gson();
    private String literal = "M";

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public void setLiteral(String literal) {
        this.literal = literal;
    }

    @Override
    public void load(String name) {
        if (!isValidName(name)) {
            OverlayMessageManger.getInstance().showInvalidNameMessage(name);
            PASExecutor.execute(() -> SkinManger.getInstance().getDataManager().invalidateData(name));
            return;
        }
        initializeDownload(name);
        PASExecutor.execute(() -> downloadProfile(name));
    }

    private void initializeDownload(String name) {
        PASClient.LOGGER.info("MojangSkinProvider: Downloading skin for " + name);
        PASModelData data = new PASModelData(name);
        OverlayMessageManger.getInstance().showDownloadMessage(name);
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void downloadProfile(String name) {
        RestHelper.get(MOJANG_API_URL + name)
                .thenApply(response -> processSimpleProfile(response, name))
                .exceptionally(throwable -> {
                    doFail(name);
                    PASClient.LOGGER.error("MojangSkinProvider: Failed to download skin for " + name, throwable);
                    return null;
                });
    }

    private CompletableFuture<?> processSimpleProfile(String response, String name) {
        SimpleProfile simpleProfile = gson.fromJson(response, SimpleProfile.class);
        if (simpleProfile == null || simpleProfile.id == null) {
            doFail(name);
            return null;
        }
        return downloadTexturedProfile(simpleProfile.id, name);
    }

    private CompletableFuture<?> downloadTexturedProfile(String uuid, String name) {
        return RestHelper.get(SESSION_SERVER_URL + uuid)
                .thenApply(response -> processTexturedProfile(response, name))
                .exceptionally(throwable -> {
                    doFail(name);
                    return null;
                });
    }

    private Void processTexturedProfile(String response, String name) {
        Profile profile = gson.fromJson(response, Profile.class);
        if (!isValidProfile(profile)) {
            doFail(name);
            return null;
        }

        String encodedSkin = StringUtils.decodeBase64(profile.properties[0].value);
        String encodedName = StringUtils.encodeToSha256(name);
        TexturedProfile texturedProfile = gson.fromJson(encodedSkin, TexturedProfile.class);

        if (!isValidTexturedProfile(texturedProfile)) {
            doFail(name);
            return null;
        }

        processSkinTexture(texturedProfile, encodedName, name);
        processCapeTexture(texturedProfile, encodedName, name);
        return null;
    }

    private void processSkinTexture(TexturedProfile profile, String encodedName, String name) {
        if (profile.textures.SKIN != null && profile.textures.SKIN.url != null) {
            Identifier skinIdentifier = Identifier.of("pas", "skins/" + encodedName);
            downloadAndRegisterTexture(skinIdentifier, encodedName + ".png", profile.textures.SKIN.url, true, name, true);
        }
    }

    private void processCapeTexture(TexturedProfile profile, String encodedName, String name) {
        if (profile.textures.CAPE != null && profile.textures.CAPE.url != null) {
            Identifier capeIdentifier = Identifier.of("pas", "capes/" + encodedName);
            downloadAndRegisterTexture(capeIdentifier, encodedName + "_cape.png", profile.textures.CAPE.url, false, name, false);
        }
    }

    private void downloadAndRegisterTexture(Identifier identifier, String fileName, String url, boolean isSkin, String name, boolean updateStatus) {
        PASSkinDownloader.downloadAndRegister(identifier, MojangDiskCache.CACHE_PATH.resolve(fileName), url, isSkin)
                .thenAccept(textureId -> updateModelData(name, textureId, isSkin))
                .thenAccept(ignored -> {
                    if (updateStatus) {
                        OverlayMessageManger.getInstance().showSuccessMessage(name);
                        PASClient.LOGGER.info("MojangSkinProvider: Successfully downloaded skin for " + name);
                        updateStatus(name, DownloadStatus.COMPLETED);
                    }
                });
    }

    private void updateModelData(String name, Identifier textureId, boolean isSkin) {
        PASModelData data = getOrCreateModelData(name);
        if (isSkin) {
            data.setSkinTexture(textureId);
        } else {
            data.setCapeTexture(textureId);
        }
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void updateStatus(String name, DownloadStatus status) {
        PASModelData data = getOrCreateModelData(name);
        data.setStatus(status);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private PASModelData getOrCreateModelData(String name) {
        PASModelData data = SkinManger.getInstance().getData(Text.of(name));
        return data != null ? data : new PASModelData(name);
    }

    private boolean isValidProfile(Profile profile) {
        return profile != null && profile.id != null &&
                profile.properties != null && profile.properties.length > 0;
    }

    private boolean isValidTexturedProfile(TexturedProfile profile) {
        return profile != null && profile.textures != null;
    }

    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() <= MAX_USERNAME_LENGTH && name.matches(USERNAME_PATTERN);
    }

    private void doFail(String name) {
        PASModelData data = SkinManger.getInstance().getData(Text.of(name));
        if (data == null) {
            data = new PASModelData(name);
        }
        OverlayMessageManger.getInstance().showFailMessage(name);
        data.setStatus(DownloadStatus.FAILED);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    class SimpleProfile {
        public String id;
        public String name;
    }

    class Profile{
        public String id;
        public String name;
        public ProfileProperty[] properties;

        class ProfileProperty {
            public String name;
            public String value;
        }
    }

    class TexturedProfile {

        public Textures textures;

        class Textures {

            public Texture SKIN;
            public Texture CAPE;

            class Texture {
                public String url;
            }
        }
    }
}
