package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.*;
import com.danrus.pas.utils.TextureUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PasManager {

    private static PasManager INSTANCE;

    private SkinDataManager dataManager;
    private SkinProvidersManager skinProviderManager;
    private List<String> existingProviders;

    private String currentName;
    private SkinData currentData;

    private PasManager() {
        // Инициализируем список провайдеров первым
        existingProviders = new ArrayList<>(List.of("F"));

        // Создаем менеджеры
        dataManager = new SkinDataManager();
        skinProviderManager = new SkinProvidersManager();

        // Инициализируем провайдеры, передавая ссылку на себя
        skinProviderManager.initialize(this);
    }

    public ResourceLocation getSkinTexture(NameInfo info) {
        if (info.isEmpty()) {
            return SkinData.DEFAULT_TEXTURE;
        }
        if (info.overlay().isEmpty()) {
            return dataManager.getData(info).getSkinTexture();
        }
        return dataManager.getData(info).getSkinTexture(info);
    }

    public ResourceLocation getCapeTexture(NameInfo info) {
        if ("M".equals(info.capeProvider())) {
            return getData(info).getCapeTexture(info);
        }
        return null;
    }

    public SkinData getData(NameInfo info) {
        if (info.isEmpty()) {
            return new SkinData("default");
        }

        if(currentData != null && !currentName.isEmpty() && currentName.equals(info.base()) ) {
            return currentData;
        } else {
            currentName = info.base();
        }
        currentData = dataManager.getData(info);
        return currentData;
    }

    public SkinData findData(NameInfo info) {
        if (info.isEmpty()) {
            return null;
        }

        return getDataManager().findData(info);
    }

    public void dropCache() {
        currentName = null;
        currentData = null;

        existingProviders = new ArrayList<>(List.of("F"));
        dataManager = new SkinDataManager();
        skinProviderManager = new SkinProvidersManager();

        // Реинициализируем провайдеры
        skinProviderManager.initialize(this);

        PlayerArmorStandsClient.LOGGER.info("SkinManger: Dropped all cached data");
    }

    public void reloadData(String string){
        NameInfo info = NameInfo.parse(string);
        getDataManager().delete(info);
        TextureUtils.clearOverlayCacheFor(string);
        if (info.isEmpty()) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: Cannot reload data for an empty name");
            return;
        }
        if (dataManager.getData(info) == null) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: No data found for " + info.base() + ", reloading from providers");
            return;
        }
    }

    public void reloadFailed() {
        PlayerArmorStandsClient.LOGGER.info("SkinManger: Reloading failed skins");
        dataManager.getSources().forEach((key, source) -> {
            source.getAll().forEach((dataKey, data) -> {
                if (data.getStatus() == DownloadStatus.FAILED) {
                    PlayerArmorStandsClient.LOGGER.info("SkinManger: Reloading failed skin for " + dataKey);
                    data.setStatus(DownloadStatus.NOT_STARTED);
                    skinProviderManager.download(NameInfo.parse(dataKey));
                }
            });
        });
    }

    public static PasManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PasManager();
        }
        return INSTANCE;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public TextureProvidersManager getSkinProviderManager() {
        return skinProviderManager;
    }

    public List<String> getExistingProviders() {
        return existingProviders;
    }
}
