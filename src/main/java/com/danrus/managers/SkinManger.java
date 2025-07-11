package com.danrus.managers;

import com.danrus.PASClient;
import com.danrus.PASModelData;
import com.danrus.enums.DownloadStatus;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkinManger {

    private static final SkinManger INSTANCE = new SkinManger();

    private DataManager dataManager = new DataManager();
    private SkinProvidersManager skinProviderManager = new SkinProvidersManager();

    public Identifier getSkinTexture(Text name) {
        if (name == null || name.getString().isEmpty()) {
            return PASModelData.DEFAULT_TEXTURE;
        }
        return dataManager.getData(name.getString()).getSkinTexture();
    }

    public PASModelData getData(Text name) {
        if (name == null || name.getString().isEmpty()) {
            PASClient.LOGGER.warn("SkinManger: getData called with null Text. Something can be wrong!");
            return new PASModelData("default");
        }
        return dataManager.getData(name.getString());
    }

    public void reloadData(String name){
        if (name == null || name.isEmpty()) {
            PASClient.LOGGER.warn("SkinProvidersManager: Cannot reload data for an empty name");
            return;
        }
        if (dataManager.getData(name) == null) {
            PASClient.LOGGER.warn("SkinProvidersManager: No data found for " + name + ", reloading from providers");
            return;
        }
        dataManager.getData(name).setStatus(DownloadStatus.NOT_STARTED);
        skinProviderManager.download(name);
    }

    public void reloadFailed() {
        PASClient.LOGGER.info("SkinManger: Reloading failed skins");
        dataManager.getSources().forEach((key, source) -> {
            source.getAll().forEach((dataKey, obj) -> {
                PASModelData data = (PASModelData) obj;
                if (data.getStatus() == DownloadStatus.FAILED) {
                    PASClient.LOGGER.info("SkinManger: Reloading failed skin for " + dataKey);
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
