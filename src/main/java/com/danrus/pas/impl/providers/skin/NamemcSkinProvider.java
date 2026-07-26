package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;

public class NamemcSkinProvider extends AbstractNamemcProvider<SkinData> {
    @Override
    protected Class<? extends DataHolder> getDataHolderClass() { return SkinData.class; }

    @Override
    protected String getNamemcId(NameInfo info) { return info.base(); }

    @Override
    protected boolean shouldRemap() { return true; }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected SkinData createDataHolder() {
        return new SkinData();
    }
}
