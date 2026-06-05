package com.danrus.pas.impl.providers.common;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.TextureProvider;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.managers.OverlayMessageManger;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
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
        this.output = getOutputString(info);
        initializeDownload(info);
        ModExecutor.execute(() -> getDownloadTask(info)
                .thenApply(identifier -> {
                    updateStatus(info, DownloadStatus.COMPLETED);
                    updateSkinData(info, identifier);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                    return null;
                })
                .exceptionally((throwable -> {
                    doFail(info);
                    onComplete.accept(output);
                    PlayerArmorStandsClient.LOGGER.error("NamemcProvider: Failed to download skin for " + info, throwable);
                    return null;
                })));
    }

    private void initializeDownload(NameInfo info) {
        PlayerArmorStandsClient.LOGGER.info("NamemcProvider: Downloading for " + info);
        T data = createDataHolder();
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        getDataManager().store(info, data);
    }

    private void updateStatus(NameInfo info, DownloadStatus status) {
        T data = getOrCreateDataHolder(info);
        data.setStatus(status);
        getDataManager().store(info, data);
    }

    private void doFail(NameInfo info) {
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        getDataManager().invalidateData(info);
    }

    protected T getOrCreateDataHolder(NameInfo info) {
        return getDataFromNamemcRepository(info).orElseGet(this::createDataHolder);
    }

    protected abstract CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info);
    protected abstract DataRepository<T> getDataManager();
    protected abstract T createDataHolder();
    protected abstract void updateSkinData(NameInfo info, ResourceLocation texture);
    protected abstract Optional<T> getDataFromNamemcRepository(NameInfo info);
    protected abstract String getOutputString(NameInfo info);
}
