package com.danrus.pas.impl.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.utils.*;
import com.danrus.pas.impl.data.MojangDiskData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.SkinManager;
import com.google.gson.Gson;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MojangSkinProvider implements SkinProvider {

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SESSION_SERVER_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";

    private final Gson gson = new Gson();
    private String literal = "M";
    private Consumer<String> onComplete;
    private String output;

    @Override
    public String getLiteral() {
        return literal;
    }


    @Override
    public void load(NameInfo info, Consumer<String> onComplete) {
        this.onComplete = onComplete;
        this.output = info.base();

        if (!isValidName(info.base())) {
            OverlayMessageManger.getInstance().showInvalidNameMessage(info.base());
            ModExecutor.execute(() -> SkinManager.getInstance().getDataManager().invalidateData(info));
            return;
        }
        initializeDownload(info);
        ModExecutor.execute(() -> downloadProfile(info));
    }

    private void initializeDownload(NameInfo info) {

        PlayerArmorStandsClient.LOGGER.info("MojangSkinProvider: Downloading skin for " + info);
        SkinData data = new SkinData(info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private void downloadProfile(NameInfo info) {
        RestHelper.get(MOJANG_API_URL + info.base())
                .thenApply(response -> processSimpleProfile(response, info))
                .exceptionally(throwable -> {
                    doFail(info);
                    PlayerArmorStandsClient.LOGGER.error("MojangSkinProvider: Failed to download skin for " + info, throwable);
                    return null;
                });
    }

    private CompletableFuture<?> processSimpleProfile(String response, NameInfo info) {
        SimpleProfile simpleProfile = gson.fromJson(response, SimpleProfile.class);
        if (simpleProfile == null || simpleProfile.id == null) {
            doFail(info);
            return null;
        }
        return downloadTexturedProfile(simpleProfile.id, info);
    }

    private CompletableFuture<?> downloadTexturedProfile(String uuid, NameInfo info) {
        return RestHelper.get(SESSION_SERVER_URL + uuid)
                .thenApply(response -> processTexturedProfile(response, info))
                .exceptionally(throwable -> {
                    doFail(info);
                    return null;
                });
    }

    private Void processTexturedProfile(String response, NameInfo info) {
        Profile profile = gson.fromJson(response, Profile.class);
        if (!isValidProfile(profile)) {
            doFail(info);
            return null;
        }

        String encodedSkin = StringUtils.decodeBase64(profile.properties[0].value);
        String encodedName = StringUtils.encodeToSha256(info.base());
        TexturedProfile texturedProfile = gson.fromJson(encodedSkin, TexturedProfile.class);

        if (!isValidTexturedProfile(texturedProfile)) {
            doFail(info);
            return null;
        }

        processSkinTexture(texturedProfile, encodedName, info);
        processCapeTexture(texturedProfile, encodedName, info);
        return null;
    }

    private void processSkinTexture(TexturedProfile profile, String encodedName, NameInfo info) {
        if (profile.textures.SKIN != null && profile.textures.SKIN.url != null) {
            ResourceLocation skinIdentifier = Rl.pas("skins/" + encodedName);
            downloadAndRegisterTexture(skinIdentifier, encodedName + ".png", profile.textures.SKIN.url, true, info, true);
        }
    }

    private void processCapeTexture(TexturedProfile profile, String encodedName, NameInfo info) {
        if (profile.textures.CAPE != null && profile.textures.CAPE.url != null) {
            ResourceLocation capeIdentifier = Rl.pas("capes/" + encodedName);
            downloadAndRegisterTexture(capeIdentifier, encodedName + "_cape.png", profile.textures.CAPE.url, false, info, false);
        }
    }

    private void downloadAndRegisterTexture(ResourceLocation identifier, String fileName, String url, boolean isSkin, NameInfo info, boolean updateStatus) {
        SkinDownloader.downloadAndRegister(identifier, MojangDiskData.CACHE_PATH.resolve(fileName), url, isSkin)
                .thenAccept(textureId -> updateModelData(info, textureId, isSkin))
                .thenAccept(ignored -> {
                    if (updateStatus) {
                        OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                        PlayerArmorStandsClient.LOGGER.info("MojangSkinProvider: Successfully downloaded skin for " + info);
                        onComplete.accept(this.output);
                        updateStatus(info, DownloadStatus.COMPLETED);
                    }
                });
    }

    private void updateModelData(NameInfo info, ResourceLocation textureId, boolean isSkin) {
        SkinData data = getOrCreateModelData(info);
        if (isSkin) {
            data.setSkinTexture(textureId);
        } else {
            data.setCapeTexture(textureId);
        }
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private void updateStatus(NameInfo info, DownloadStatus status) {
        SkinData data = getOrCreateModelData(info);
        data.setStatus(status);
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private SkinData getOrCreateModelData(NameInfo info) {
        SkinData data = SkinManager.getInstance().getData(info);
        return data != null ? data : new SkinData(info);
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

    private void doFail(NameInfo info) {
        SkinData data = SkinManager.getInstance().getData(info);
        if (data == null) {
            data = new SkinData(info);
        }
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        data.setStatus(DownloadStatus.FAILED);
        SkinManager.getInstance().getDataManager().store(info, data);
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
