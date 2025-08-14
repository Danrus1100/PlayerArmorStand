package com.danrus.pas.utils.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.utils.RestHelper;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.data.MojangDiskCache;
import com.danrus.pas.utils.managers.DataManagerImpl;
import com.danrus.pas.utils.managers.OverlayMessageManger;
import com.danrus.pas.utils.managers.SkinManger;
import com.danrus.pas.utils.managers.SkinProvidersManagerImpl;
import com.google.gson.Gson;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
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
    public void load(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();

        if (!isValidName(name)) {
            OverlayMessageManger.getInstance().showInvalidNameMessage(name);
            ModExecutor.execute(() -> SkinManger.getInstance().getDataManager().invalidateData(name));
            return;
        }
        initializeDownload(name);
        ModExecutor.execute(() -> downloadProfile(name));
    }

    private void initializeDownload(String name) {
        PlayerArmorStandsClient.LOGGER.info("MojangSkinProvider: Downloading skin for " + name);
        SkinData data = new SkinData(name);
        OverlayMessageManger.getInstance().showDownloadMessage(name);
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void downloadProfile(String name) {
        RestHelper.get(MOJANG_API_URL + name)
                .thenApply(response -> processSimpleProfile(response, name))
                .exceptionally(throwable -> {
                    doFail(name);
                    PlayerArmorStandsClient.LOGGER.error("MojangSkinProvider: Failed to download skin for " + name, throwable);
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
            ResourceLocation skinIdentifier = VersioningUtils.getResourceLocation("pas", "skins/" + encodedName);
            downloadAndRegisterTexture(skinIdentifier, encodedName + ".png", profile.textures.SKIN.url, true, name, true);
        }
    }

    private void processCapeTexture(TexturedProfile profile, String encodedName, String name) {
        if (profile.textures.CAPE != null && profile.textures.CAPE.url != null) {
            ResourceLocation capeIdentifier =VersioningUtils.getResourceLocation("pas", "capes/" + encodedName);
            downloadAndRegisterTexture(capeIdentifier, encodedName + "_cape.png", profile.textures.CAPE.url, false, name, false);
        }
    }

    private void downloadAndRegisterTexture(ResourceLocation identifier, String fileName, String url, boolean isSkin, String name, boolean updateStatus) {
        SkinDownloader.downloadAndRegister(identifier, MojangDiskCache.CACHE_PATH.resolve(fileName), url, isSkin)
                .thenAccept(textureId -> updateModelData(name, textureId, isSkin))
                .thenAccept(ignored -> {
                    if (updateStatus) {
                        OverlayMessageManger.getInstance().showSuccessMessage(name);
                        PlayerArmorStandsClient.LOGGER.info("MojangSkinProvider: Successfully downloaded skin for " + name);
                        updateStatus(name, DownloadStatus.COMPLETED);
                    }
                });
    }

    private void updateModelData(String name, ResourceLocation textureId, boolean isSkin) {
        SkinData data = getOrCreateModelData(name);
        if (isSkin) {
            data.setSkinTexture(textureId);
        } else {
            data.setCapeTexture(textureId);
        }
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void updateStatus(String name, DownloadStatus status) {
        SkinData data = getOrCreateModelData(name);
        data.setStatus(status);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private SkinData getOrCreateModelData(String name) {
        SkinData data = SkinManger.getInstance().getData(Component.literal(name));
        return data != null ? data : new SkinData(name);
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
        SkinData data = SkinManger.getInstance().getData(Component.literal(name));
        if (data == null) {
            data = new SkinData(name);
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
