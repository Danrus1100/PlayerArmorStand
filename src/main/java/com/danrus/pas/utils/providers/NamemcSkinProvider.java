package com.danrus.pas.utils.providers;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.data.NamemcDiskCache;
import com.danrus.pas.utils.managers.OverlayMessageManger;
import com.danrus.pas.utils.managers.SkinManger;
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
    public void load(String string, Consumer<String> onComplete) {
        this.onComplete = onComplete;
        this.output = string;
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();
        initializeDownload(name);
        ModExecutor.execute(() -> SkinDownloader.downloadAndRegister(
                        VersioningUtils.getResourceLocation("pas", "skins/" + name),
                        NamemcDiskCache.CACHE_PATH.resolve(name + "_namemc.png"),
                        "https://s.namemc.com/i/" + name + ".png",
                        true
                )
                .thenApply(identifier -> {
                    updateStatus(string, DownloadStatus.COMPLETED);
                    updateSkinData(string, identifier, true);
                    onComplete.accept(output);
                    OverlayMessageManger.getInstance().showSuccessMessage(name);
                    return null;
                })
                .exceptionally((throwable -> {
                    doFail(string);
                    PlayerArmorStandsClient.LOGGER.error("NamemcSkinProvider: Failed to download skin for " + name, throwable);
                    return null;
                })));
    }

    private void initializeDownload(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();
        PlayerArmorStandsClient.LOGGER.info("NamemcSkinProvider: Downloading skin for " + name);
        SkinData data = new SkinData(name);
        OverlayMessageManger.getInstance().showDownloadMessage(name);
        data.setStatus(DownloadStatus.IN_PROGRESS);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void updateStatus(String string, DownloadStatus status) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();
        SkinData data = getOrCreateModelData(string);
        data.setStatus(status);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void doFail(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();
        SkinData data = SkinManger.getInstance().getData(Component.literal(name));
        if (data == null) {
            data = new SkinData(name);
        }
        OverlayMessageManger.getInstance().showFailMessage(name);
        data.setStatus(DownloadStatus.FAILED);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    private void updateSkinData(String string, ResourceLocation texture, boolean isSkin) {
        SkinData data = getOrCreateModelData(string);
        if (isSkin) {
            data.setSkinTexture(texture);
        } else {
            data.setCapeTexture(texture);
        }
        SkinManger.getInstance().getDataManager().store(string, data);
    }

    private SkinData getOrCreateModelData(String string) {
//        SkinData data = SkinManger.getInstance().getData(Component.literal(string));
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        SkinData data = SkinManger.getInstance().getDataManager().getSource("namemc").get(name);
        return data != null ? data : new SkinData(string);
    }
}