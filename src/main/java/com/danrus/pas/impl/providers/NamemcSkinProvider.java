package com.danrus.pas.impl.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.impl.data.NamemcDiskData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.SkinManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class NamemcSkinProvider implements SkinProvider {

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
        ModExecutor.execute(() -> SkinDownloader.downloadAndRegister(
                                Rl.pas( "skins/" + info.base()),
                        NamemcDiskData.CACHE_PATH.resolve(info.base() + "_namemc.png"),
                        "https://s.namemc.com/i/" + info.base() + ".png",
                        true
                )
                .thenApply(identifier -> {
                    updateStatus(info, DownloadStatus.COMPLETED);
                    updateSkinData(info, identifier, true);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showSuccessMessage(info.base());
                    return null;
                })
                .exceptionally((throwable -> {
                    doFail(info);
                    PlayerArmorStandsClient.LOGGER.error("NamemcSkinProvider: Failed to download skin for " + info, throwable);
                    return null;
                })));
    }

    private void initializeDownload(NameInfo info) {
        PlayerArmorStandsClient.LOGGER.info("NamemcSkinProvider: Downloading skin for " + info);
        SkinData data = new SkinData(info);
        OverlayMessageManger.getInstance().showDownloadMessage(info.base());
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private void updateStatus(NameInfo info, DownloadStatus status) {
        SkinData data = getOrCreateModelData(info);
        data.setStatus(status);
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private void doFail(NameInfo info) {
        SkinData data = SkinManager.getInstance().getData(info);
        if (data == null) {
            data = new SkinData(info);
        }
        OverlayMessageManger.getInstance().showFailMessage(info.base());
        data.setStatus(DownloadStatus.FAILED);
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private void updateSkinData(NameInfo info, ResourceLocation texture, boolean isSkin) {
        SkinData data = getOrCreateModelData(info);
        if (isSkin) {
            data.setSkinTexture(texture);
        } else {
            data.setCapeTexture(texture);
        }
        SkinManager.getInstance().getDataManager().store(info, data);
    }

    private SkinData getOrCreateModelData(NameInfo info) {
        SkinData data = SkinManager.getInstance().getDataManager().getSource("namemc").get(info);
        return data != null ? data : new SkinData(info);
    }
}