package com.danrus.pas.impl.providers.common;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.TextureProvider;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.managers.OverlayMessageManger;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractNamemcProvider<T extends DataHolder> implements TextureProvider {

    private final String literal = "N";

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public void load(NameInfo info, Consumer<String> onComplete) {
        final String output = getOutputString(info);

        T data = getDataManager().findData(info);

        PlayerArmorStandsClient.LOGGER.info("NamemcProvider: Starting download for " + info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());

        ModExecutor.execute(() -> getDownloadTask(info)
                .thenApply(identifier -> {
                    data.setStatus(DownloadStatus.COMPLETED);
                    updateSkinData(info, identifier);
                    getDataManager().store(info, data);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());

                    return null;
                })
                .exceptionally((throwable -> {
                    data.setStatus(DownloadStatus.FAILED);
                    getDataManager().store(info, data);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showFailMessage(info.base());
                    PlayerArmorStandsClient.LOGGER.error("NamemcProvider: Failed to download skin for " + info, throwable);
                    return null;
                })));
    }

    protected abstract CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info);
    protected abstract DataRepository<T> getDataManager();
    protected abstract void updateSkinData(NameInfo info, ResourceLocation texture);
    protected abstract String getOutputString(NameInfo info);
}