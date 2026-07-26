package com.danrus.pas.impl.providers.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.TextureProvider;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.utils.net.TextureDownloader;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractNamemcProvider<T extends DataHolder> implements TextureProvider {

    private final String literal = "N";

    @Override
    public String getLiteral() {
        return literal;
    }


    @Override
    public CompletableFuture<Void> load(NameInfo info) {
        initializeDownload(info);
        return getDownloadTask(info)
                .thenAccept(identifier -> {
                    T data = createDataHolder();
                    data.setTexture(identifier);
                    data.setStatus(DownloadStatus.COMPLETED);
                    getDataManager().store(info, data);
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                })
                .whenComplete((ignored, throwable) -> {
                    if (throwable != null) {
                        doFail(info);
                        PlayerArmorStandsClient.LOGGER.error(
                                "NamemcProvider: Failed to download texture for " + info,
                                throwable);
                    }
                });
    }

    private void initializeDownload(NameInfo info) {
        PlayerArmorStandsClient.LOGGER.info("NamemcProvider: Downloading for " + info);
        T data = createDataHolder();
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        getDataManager().store(info, data);
    }

    private void doFail(NameInfo info) {
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        getDataManager().invalidateData(info);
    }

    protected CompletableFuture<Identifier> getDownloadTask(NameInfo info) {
        Identifier location = InfoTranslators.getInstance().toIdentifier(getDataHolderClass(), info);
        String fileName = InfoTranslators.getInstance().toFileName(getDataHolderClass(), info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");
        return TextureDownloader.downloadAndRegister(location, filePath, "https://s.namemc.com/i/" + getNamemcId(info) + ".png", shouldRemap());
    }

    protected abstract Class<? extends DataHolder> getDataHolderClass();
    protected abstract String getNamemcId(NameInfo info);
    protected abstract boolean shouldRemap();
    protected abstract DataRepository<T> getDataManager();
    protected abstract T createDataHolder();
}
