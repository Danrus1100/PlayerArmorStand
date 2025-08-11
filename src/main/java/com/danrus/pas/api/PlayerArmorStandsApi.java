package com.danrus.pas.api;

import com.danrus.pas.utils.managers.SkinManger;

/**
 * PlayerArmorStandsApi provides access to the data manager and skin provider manager.
 * It allows retrieval of skin data by player name.
 * !!!WARNING!!!: in 0.X.X versions, this API is unstable and may change in future releases and betas.
 */

public class PlayerArmorStandsApi {
    public DataManager getDataManager() {
        return SkinManger.getInstance().getDataManager();
    }

    public SkinProvidersManager getSkinProviderManager() {
        return SkinManger.getInstance().getSkinProviderManager();
    }

    public SkinData getSkinData(String name) {
        return getDataManager().getData(name);
    }
}
