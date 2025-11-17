package com.danrus.pas.impl.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.data.TextureProvider;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.RestHelper;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.utils.StringUtils;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class MojangProvider implements TextureProvider {

    private static final MojangProvider INSTANCE = new MojangProvider();

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SESSION_SERVER_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";

    private final Map<String, CompletableFuture<Void>> activeDownloads = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();
    private final String literal = "M";

    private MojangProvider() {}

    public static MojangProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public void load(NameInfo info, Consumer<String> onComplete) {
        synchronized (activeDownloads) {
            CompletableFuture<Void> existing = activeDownloads.get(info.base());
            if (existing != null) {
                existing.thenAccept(v -> onComplete.accept(info.base()));
                PlayerArmorStandsClient.LOGGER.info("MojangProvider: Reusing active download for " + info.base());
                return;
            }
        }

        if (!isValidName(info.base())) {
            OverlayMessageManger.getInstance().showInvalidNameMessage(info.base());
            ModExecutor.execute(() -> invalidateAllData(info));
            onComplete.accept(info.base());
            return;
        }

        initializeDownload(info);
        CompletableFuture<Void> downloadFuture = downloadProfile(info, onComplete);

        synchronized (activeDownloads) {
            activeDownloads.put(info.base(), downloadFuture);
        }

        downloadFuture.whenComplete((v, throwable) -> {
            synchronized (activeDownloads) {
                activeDownloads.remove(info.base());
            }
        });
    }

    private void initializeDownload(NameInfo info) {
        PlayerArmorStandsClient.LOGGER.info("MojangProvider: Downloading textures for " + info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());

        if (info.getFeature(SkinProviderFeature.class).getProvider().equals(getLiteral())) {
            SkinData skinData = new SkinData(info);
            skinData.setStatus(DownloadStatus.IN_PROGRESS);
            PasManager.getInstance().getSkinDataManager().store(info, skinData);
        }

        if (info.getFeature(CapeFeature.class).getProvider().equals(getLiteral())) {
            CapeData capeData = new CapeData(info);
            capeData.setStatus(DownloadStatus.IN_PROGRESS);
            PasManager.getInstance().getCapeDataManager().store(info, capeData);
        }
    }

    private CompletableFuture<Void> downloadProfile(NameInfo info, Consumer<String> onComplete) {
        return RestHelper.get(MOJANG_API_URL + info.base())
                .thenCompose(response -> processSimpleProfile(response, info))
                .thenCompose(uuid -> downloadTexturedProfile(uuid, info))
                .thenCompose(texturedProfile -> processTexturedProfile(texturedProfile, info, onComplete))
                .exceptionally(throwable -> {
                    doFail(info);
                    PlayerArmorStandsClient.LOGGER.error("MojangProvider: Failed to download for " + info, throwable);
                    return null;
                });
    }

    private CompletableFuture<String> processSimpleProfile(String response, NameInfo info) {
        SimpleProfile simpleProfile = gson.fromJson(response, SimpleProfile.class);
        if (simpleProfile == null || simpleProfile.id == null) {
            doFail(info);
            return CompletableFuture.failedFuture(new RuntimeException("Invalid simple profile"));
        }
        return CompletableFuture.completedFuture(simpleProfile.id);
    }

    private CompletableFuture<TexturedProfile> downloadTexturedProfile(String uuid, NameInfo info) {
        return RestHelper.get(SESSION_SERVER_URL + uuid)
                .thenApply(response -> {
                    Profile profile = gson.fromJson(response, Profile.class);
                    if (!isValidProfile(profile)) {
                        throw new RuntimeException("Invalid profile");
                    }

                    String encodedSkin = StringUtils.decodeBase64(profile.properties[0].value);
                    TexturedProfile texturedProfile = gson.fromJson(encodedSkin, TexturedProfile.class);

                    if (!isValidTexturedProfile(texturedProfile)) {
                        throw new RuntimeException("Invalid textured profile");
                    }

                    return texturedProfile;
                });
    }

    private CompletableFuture<Void> processTexturedProfile(TexturedProfile profile, NameInfo info, Consumer<String> onComplete) {
        CompletableFuture<Void> skinFuture = processSkinTexture(profile, info);
        CompletableFuture<Void> capeFuture = processCapeTexture(profile, info);

        return CompletableFuture.allOf(skinFuture, capeFuture)
                .thenRun(() -> {
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                    PlayerArmorStandsClient.LOGGER.info("MojangProvider: Successfully downloaded textures for " + info);
                    onComplete.accept(info.base());
                });
    }

    private CompletableFuture<Void> processSkinTexture(TexturedProfile profile, NameInfo info) {
        if (!info.getFeature(SkinProviderFeature.class).getProvider().equals("M")) {
            return CompletableFuture.completedFuture(null);
        }
        PlayerArmorStandsClient.LOGGER.info("processSkinTexture called for {}", info);
        if (profile.textures.SKIN == null || profile.textures.SKIN.url == null) {
            SkinData data = new SkinData(info);
            data.setStatus(DownloadStatus.FAILED);
            PasManager.getInstance().getSkinDataManager().store(info, data);
            return CompletableFuture.completedFuture(null);
        }

        ResourceLocation skinLocation = InfoTranslators.getInstance()
                .toResourceLocation(SkinData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(SkinData.class, info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");

        return SkinDownloader.downloadAndRegister(skinLocation, filePath, profile.textures.SKIN.url, true)
                .thenAccept(textureId -> {
                    SkinData data = new SkinData(info);
                    data.setTexture(textureId);
                    data.setStatus(DownloadStatus.COMPLETED);
                    PasManager.getInstance().getSkinDataManager().store(info, data);
                });
    }

    private CompletableFuture<Void> processCapeTexture(TexturedProfile profile, NameInfo info) {
        if (!info.getFeature(CapeFeature.class).getProvider().equals("M")) {
            return CompletableFuture.completedFuture(null);
        }
        PlayerArmorStandsClient.LOGGER.info("processCapeTexture called for {}", info);
        if (profile.textures.CAPE == null || profile.textures.CAPE.url == null) {
            CapeData data = new CapeData(info);
            data.setStatus(DownloadStatus.COMPLETED);
            PasManager.getInstance().getCapeDataManager().store(info, data);
            return CompletableFuture.completedFuture(null);
        }

        ResourceLocation capeLocation = InfoTranslators.getInstance()
                .toResourceLocation(CapeData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(CapeData.class, info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");

        return SkinDownloader.downloadAndRegister(capeLocation, filePath, profile.textures.CAPE.url, false)
                .thenAccept(textureId -> {
                    CapeData data = new CapeData(info);
                    data.setTexture(textureId);
                    data.setStatus(DownloadStatus.COMPLETED);
                    PasManager.getInstance().getCapeDataManager().store(info, data);
                });
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
        OverlayMessageManger.getInstance().showFailMessage(info.base());

        SkinData skinData = new SkinData(info);
        skinData.setStatus(DownloadStatus.FAILED);
        PasManager.getInstance().getSkinDataManager().store(info, skinData);

        CapeData capeData = new CapeData(info);
        capeData.setStatus(DownloadStatus.FAILED);
        PasManager.getInstance().getCapeDataManager().store(info, capeData);
    }

    private void invalidateAllData(NameInfo info) {
        PasManager.getInstance().getSkinDataManager().invalidateData(info);
        PasManager.getInstance().getCapeDataManager().invalidateData(info);
    }

    // Inner classes
    static class SimpleProfile {
        public String id;
        public String name;
    }

    static class Profile {
        public String id;
        public String name;
        public ProfileProperty[] properties;

        static class ProfileProperty {
            public String name;
            public String value;
        }
    }

    static class TexturedProfile {
        public Textures textures;

        static class Textures {
            public Texture SKIN;
            public Texture CAPE;

            static class Texture {
                public String url;
            }
        }
    }
}
