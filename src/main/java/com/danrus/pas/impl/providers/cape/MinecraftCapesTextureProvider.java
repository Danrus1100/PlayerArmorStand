package com.danrus.pas.impl.providers.cape;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.data.TextureProvider;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.MojangUtils;
import com.danrus.pas.utils.RestHelper;
import com.danrus.pas.utils.SkinDownloader;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.danrus.pas.impl.data.common.AbstractDiskDataProvider.CACHE_PATH;

public class MinecraftCapesTextureProvider implements TextureProvider{

    private static final String MINECRAFT_CAPES_API_URL="https://api.minecraftcapes.net/profile/";
    private final Gson gson = new Gson();

    @Override
    public void load(NameInfo info, Consumer<String> onComplete) {
        MojangUtils.getUUID(info)
                .thenCompose(this::downloadProfile)
                .thenCompose(profile -> processProfile(profile, info, onComplete))
                .exceptionally(throwable -> {
                    doFail(info);
                    onComplete.accept(getOutputString(info));
                    PlayerArmorStandsClient.LOGGER.error("MojangProvider: Failed to download for " + info, throwable);
                    return null;
                });
    }

    private CompletableFuture<Profile> downloadProfile(String uuid){
        return RestHelper.get(MINECRAFT_CAPES_API_URL + uuid).
                thenApply(response -> {
                   Profile profile = gson.fromJson(response, Profile.class);

                   if (profile.cape_url.isEmpty()) {
                       throw new RuntimeException("Invalid profile");
                   }

                   return profile;
                });
    }

    private CompletableFuture<Void> processProfile(Profile profile, NameInfo info, Consumer<String> onComplete) {
        if (!info.getFeature(CapeFeature.class).getProvider().equals("I")) {
            return CompletableFuture.completedFuture(null);
        }
        PlayerArmorStandsClient.LOGGER.info("processCapeTexture called for {}", info);
        if (profile.cape_url.isEmpty()) {
            CapeData data = new CapeData(info);
            data.setStatus(DownloadStatus.COMPLETED);
            PasManager.getInstance().getCapeDataManager().store(info, data);
            return CompletableFuture.completedFuture(null);
        }

        ResourceLocation capeLocation = InfoTranslators.getInstance()
                .toResourceLocation(CapeData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(CapeData.class, info);
        Path filePath = CACHE_PATH.resolve(fileName + ".png");

        return SkinDownloader.downloadAndRegister(capeLocation, filePath, profile.cape_url, false)
                .thenAccept(textureId -> {
                    CapeData data = new CapeData(info);
                    data.setTexture(textureId);
                    data.setStatus(DownloadStatus.COMPLETED);
                    PasManager.getInstance().getCapeDataManager().store(info, data);
                    onComplete.accept(getOutputString(info));
                });
    }

    @Override
    public String getLiteral() {
        return "I";
    }

    protected String getOutputString(NameInfo info) {
        return info.getFeature(CapeFeature.class).compile();
    }

    private void doFail(NameInfo info) {
        OverlayMessageManger.getInstance().showFailMessage(info.base());

        CapeData capeData = new CapeData(info);
        capeData.setStatus(DownloadStatus.FAILED);
        PasManager.getInstance().getCapeDataManager().store(info, capeData);
    }

    private static class Profile {
        String cape_url;
    }
}
