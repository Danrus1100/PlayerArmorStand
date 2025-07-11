package com.danrus.utils.providers;

import com.danrus.*;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.AbstractSkinProvider;
import com.danrus.utils.PASSkinDownloader;
import com.danrus.utils.data.NamemcDiskCache;
import com.danrus.managers.OverlayMessageManger;
import com.danrus.managers.SkinManger;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NamemcSkinProvider extends AbstractSkinProvider {
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
}
