package com.danrus.utils.providers;

import com.danrus.*;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.SkinProvider;
import com.danrus.utils.PASSkinDownloader;
import com.danrus.utils.data.NamemcDiskCache;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NamemcSkinProvider implements SkinProvider {

    private String literal = "N";

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
        initializeDownload(name);
        PASExecutor.execute(() -> PASSkinDownloader.downloadAndRegister(
                Identifier.of("pas", "skins/" + name),
                NamemcDiskCache.CACHE_PATH.resolve(name + "_namemc.png"),
                "https://s.namemc.com/i/" + name + ".png",
                true
        )
                .thenApply(identifier -> {
                    updateStatus(name, DownloadStatus.COMPLETED);
                    updateModelData(name, identifier, true);
                    OverlayMessageManger.getInstance().showSuccessMessage(name);
                    return null;
                })
                .exceptionally((throwable -> {
                    doFail(name);
                    PASClient.LOGGER.error("NamemcSkinProvider: Failed to download skin for " + name, throwable);
                    return null;
                })));
    }

    private void initializeDownload(String name) {
        PASClient.LOGGER.info("NamemcSkinProvider: Downloading skin for " + name);
        PASModelData data = new PASModelData(name);
        OverlayMessageManger.getInstance().showDownloadMessage(name);
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void updateStatus(String name, DownloadStatus status) {
        PASModelData data = getOrCreateModelData(name);
        data.setStatus(status);
        SkinManger.getInstance().getDataManager().store(name, data);
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

    private void updateModelData(String name, Identifier textureId, boolean isSkin) {
        PASModelData data = getOrCreateModelData(name);
        if (isSkin) {
            data.setSkinTexture(textureId);
        } else {
            data.setCapeTexture(textureId);
        }
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private PASModelData getOrCreateModelData(String name) {
        PASModelData data = SkinManger.getInstance().getData(Text.of(name));
        return data != null ? data : new PASModelData(name);
    }
}
