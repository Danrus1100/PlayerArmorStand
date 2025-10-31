package com.danrus.pas.impl.providers.common;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractNamemcProvider<T extends DataHolder> implements TextureProvider {

    private String literal = "N";
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
        initializeDownload(info);
        ModExecutor.execute(() -> getDownloadTask(info)
                .thenApply(identifier -> {
                    updateStatus(info, DownloadStatus.COMPLETED);
                    updateSkinData(info, identifier, true);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                    return null;
                })
                .exceptionally((throwable -> {
                    doFail(info);
                    PlayerArmorStandsClient.LOGGER.error("NamemcProvider: Failed to download skin for " + info, throwable);
                    return null;
                })));
    }

    private void initializeDownload(NameInfo info) {
        PlayerArmorStandsClient.LOGGER.info("NamemcProvider: Downloading for " + info);
        LegacySkinData data = new LegacySkinData(info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        getDataManager().store(info, data);
    }

    private void updateStatus(NameInfo info, DownloadStatus status) {
        LegacySkinData data = getOrCreateModelData(info);
        data.setStatus(status);
        getDataManager().store(info, data);
    }

    private void doFail(NameInfo info) {
        LegacySkinData data = PasManager.getInstance().getData(info);
        if (data == null) {
            data = new LegacySkinData(info);
        }
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        data.setStatus(DownloadStatus.FAILED);
        getDataManager().store(info, data);
    }

    private void updateSkinData(NameInfo info, ResourceLocation texture, boolean isSkin) {
        LegacySkinData data = getOrCreateModelData(info);
        if (isSkin) {
            data.setSkinTexture(texture);
        } else {
            data.setCapeTexture(texture);
        }
        getDataManager().store(info, data);
    }

    private LegacySkinData getOrCreateModelData(NameInfo info) {
        LegacySkinData data = getDataManager().getSource("namemc").get(info);
        return data != null ? data : new LegacySkinData(info);
    }

    protected abstract CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info);
    protected abstract DataRepository<T> getDataManager();
}
