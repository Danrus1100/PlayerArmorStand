package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DataManager;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvidersManager;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.data.*;
import com.danrus.pas.utils.providers.MojangSkinProvider;
import com.danrus.pas.utils.providers.NamemcSkinProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SkinManger {

    private static final SkinManger INSTANCE = new SkinManger();

    private DataManagerImpl dataManager = new DataManagerImpl();
    private SkinProvidersManagerImpl skinProviderManager = new SkinProvidersManagerImpl();

    private SkinManger() {
        getDataManager().addSource(new ClientLevelCache(), 0);
        getDataManager().addSource(new GameCache(), 2);
        getDataManager().addSource(new MojangDiskCache());
        getDataManager().addSource(new NamemcDiskCache());
        getDataManager().addSource(new FileTextureCache(), 999);

        getSkinProviderManager().addProvider(new MojangSkinProvider());
        getSkinProviderManager().addProvider(new NamemcSkinProvider());
    }

    public ResourceLocation getSkinTexture(Component name) {
        if (name == null || name.getString().isEmpty()) {
            return SkinData.DEFAULT_TEXTURE;
        }
        List <String> matches = StringUtils.matchASName(name.getString());
        String material = matches.get(2);
        String blend = matches.get(3);
        if (material == null || material.isEmpty()) {
            return dataManager.getData(name.getString()).getSkinTexture();
        }
        return dataManager.getData(name.getString()).getSkinTexture(material, blend);
    }

    public SkinData getData(Component name) {
        if (name == null || name.getString().isEmpty()) {
            return new SkinData("default");
        }
        return dataManager.getData(name.getString());
    }

    public SkinData findData(Component name) {
        if (name == null || name.getString().isEmpty()) {
            return null;
        }
        List<String> matches = StringUtils.matchASName(name.getString());
        String playerName = matches.get(0);

        return getDataManager().findData(playerName);
    }

    public void reloadData(String string){
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        getDataManager().delete(string);
        if (name == null || name.isEmpty()) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: Cannot reload data for an empty name");
            return;
        }
        if (dataManager.getData(name) == null) {
            PlayerArmorStandsClient.LOGGER.warn("SkinManger: No data found for " + name + ", reloading from providers");
            return;
        }
        skinProviderManager.download(string);
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

