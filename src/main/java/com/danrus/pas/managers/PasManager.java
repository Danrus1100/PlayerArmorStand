package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.*;
import com.danrus.pas.impl.providers.MojangSkinProvider;
import com.danrus.pas.impl.providers.NamemcSkinProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PasManager {

    private static final PasManager INSTANCE = new PasManager();

    private SkinDataManager dataManager = new SkinDataManager();
    private SkinProvidersManager skinProviderManager = new SkinProvidersManager();

    private List<String> existingProviders = new ArrayList<>(List.of("F"));

    private String currentName;
    private SkinData currentData;

    private PasManager() {
        getDataManager().addSource(new ClientLevelData(), 0);
        getDataManager().addSource(new GameData(), 2);
        getDataManager().addSource(new MojangDiskData());
        getDataManager().addSource(new NamemcDiskData());
        getDataManager().addSource(new FileTextureData(), 999);

        addProvider(new MojangSkinProvider());
        addProvider(new NamemcSkinProvider());
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
            return getData(info).getCapeTexture();
        }
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

    public void reloadData(String string){
        NameInfo info = NameInfo.parse(string);
        getDataManager().delete(info);
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
        return INSTANCE;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public TextureProvidersManager getSkinProviderManager() {
        return skinProviderManager;
    }

    public List<String> getExistingProviders() {
        return List.copyOf(existingProviders);
    }

    public void addProvider(TextureProvider provider) {
        addProvider(provider, 0);
    }

    public void addProvider(TextureProvider provider, int priority) {
        skinProviderManager.addProvider(provider, priority);
        existingProviders.add(provider.getLiteral());
    }
}

