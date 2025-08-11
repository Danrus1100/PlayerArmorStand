package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DataManager;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvidersManager;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.utils.data.GameCache;
import com.danrus.pas.utils.data.MojangDiskCache;
import com.danrus.pas.utils.data.NamemcDiskCache;
import com.danrus.pas.utils.providers.MojangSkinProvider;
import com.danrus.pas.utils.providers.NamemcSkinProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkinManger {

    private static final SkinManger INSTANCE = new SkinManger();

    private DataManagerImpl dataManager = new DataManagerImpl();
    private SkinProvidersManagerImpl skinProviderManager = new SkinProvidersManagerImpl();

    private SkinManger() {
        getDataManager().addSource(new GameCache());
        getDataManager().addSource(new MojangDiskCache());
        getDataManager().addSource(new NamemcDiskCache());

        getSkinProviderManager().addProvider(new MojangSkinProvider());
        getSkinProviderManager().addProvider(new NamemcSkinProvider());
    }

    public ResourceLocation getSkinTexture(Component name) {
        if (name == null || name.getString().isEmpty()) {
            return SkinData.DEFAULT_TEXTURE;
        }
        return dataManager.getData(name.getString()).getSkinTexture();
    }

    public SkinData getData(Component name) {
        if (name == null || name.getString().isEmpty()) {
            return new SkinData("default");
        }
        return dataManager.getData(name.getString());
    }

    public void reloadData(String name){
        if (name == null || name.isEmpty()) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: Cannot reload data for an empty name");
            return;
        }
        if (dataManager.getData(name) == null) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: No data found for " + name + ", reloading from providers");
            return;
        }
        dataManager.getData(name).setStatus(DownloadStatus.NOT_STARTED);
        skinProviderManager.download(name);
    }

    public void reloadFailed() {
        PlayerArmorStandsClient.LOGGER.info("SkinManger: Reloading failed skins");
        dataManager.getSources().forEach((key, source) -> {
            source.getAll().forEach((dataKey, obj) -> {
                SkinData data = (SkinData) obj;
                if (data.getStatus() == DownloadStatus.FAILED) {
                    PlayerArmorStandsClient.LOGGER.info("SkinManger: Reloading failed skin for " + dataKey);
                    data.setStatus(DownloadStatus.NOT_STARTED);
                    skinProviderManager.download((String) dataKey);
                }
            });
        });
    }

    public void init() {
        return;
    }

    public static SkinManger getInstance() {
        return INSTANCE;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SkinProvidersManager getSkinProviderManager() {
        return skinProviderManager;
    }
}

