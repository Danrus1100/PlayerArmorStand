package com.danrus.pas.impl.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.NamemcDiskData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.SkinDownloader;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractNamemcProvider implements TextureProvider {

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
        SkinData data = new SkinData(info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        getDataManager().store(info, data);
    }

    private void updateStatus(NameInfo info, DownloadStatus status) {
        SkinData data = getOrCreateModelData(info);
        data.setStatus(status);
        getDataManager().store(info, data);
    }

    private void doFail(NameInfo info) {
        SkinData data = PasManager.getInstance().getData(info);
        if (data == null) {
            data = new SkinData(info);
        }
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        data.setStatus(DownloadStatus.FAILED);
        getDataManager().store(info, data);
    }

    private void updateSkinData(NameInfo info, ResourceLocation texture, boolean isSkin) {
        SkinData data = getOrCreateModelData(info);
        if (isSkin) {
            data.setSkinTexture(texture);
        } else {
            data.setCapeTexture(texture);
        }
        getDataManager().store(info, data);
    }

    private SkinData getOrCreateModelData(NameInfo info) {
        SkinData data = getDataManager().getSource("namemc").get(info);
        return data != null ? data : new SkinData(info);
    }

    protected abstract CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info);
    protected abstract DataManager getDataManager();
}
