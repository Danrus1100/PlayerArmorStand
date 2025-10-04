package com.danrus.pas.api;

import com.danrus.pas.managers.SkinManager;

/**
 * PlayerArmorStandsApi provides access to the data manager and skin provider manager.
 * It allows retrieval of skin data by player name.
 * !!!WARNING!!!: in 0.X.X versions, this API is unstable and may change in future releases and betas.
 */

public class PlayerArmorStandsApi {
    public DataManager getDataManager() {
        return SkinManager.getInstance().getDataManager();
    }

    public SkinProvidersManager getSkinProviderManager() {
        return SkinManager.getInstance().getSkinProviderManager();
    }

    public SkinData getSkinData(NameInfo info) {
        return getDataManager().getData(info);
    }
}
