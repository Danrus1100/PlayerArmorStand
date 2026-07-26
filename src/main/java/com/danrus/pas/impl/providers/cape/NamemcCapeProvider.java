package com.danrus.pas.impl.providers.cape;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;

public class NamemcCapeProvider extends AbstractNamemcProvider<CapeData> {

    @Override
    public String getLiteral() {
        return "A";
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() { return CapeData.class; }

    @Override
    protected String getNamemcId(NameInfo info) { return info.getFeature(CapeFeature.class).getId(); }

    @Override
    protected boolean shouldRemap() { return false; }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }

    @Override
    protected CapeData createDataHolder() {
        return new CapeData();
    }
}
