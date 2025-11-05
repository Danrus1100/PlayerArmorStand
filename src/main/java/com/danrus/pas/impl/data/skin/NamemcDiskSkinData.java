package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataRepository;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;

public class NamemcDiskSkinData extends AbstractDiskDataProvider<SkinData> {

    @Override
    protected SkinData createDataHolder(NameInfo info) {
        return new SkinData(info);
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() {
        return SkinData.class;
    }

    @Override
    protected boolean shouldProcessSkin() {
        return true;
    }

    @Override
    protected String getKeySuffix() {
        return "";
    }

    @Override
    public String getName() {
        return "namemc";
    }
}
