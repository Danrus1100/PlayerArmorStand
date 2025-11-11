package com.danrus.pas.impl.data.cape;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.managers.PasManager;

public class MojangDiskCapeData extends AbstractDiskDataProvider<CapeData> {

    @Override
    public CapeData get(NameInfo info) {
        if ("M".equals(info.getFeature(CapeFeature.class).getProvider())) {
            return super.get(info);
        }
        return null;
    }

    @Override
    protected CapeData createDataHolder(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() {
        return CapeData.class;
    }

    @Override
    protected boolean shouldProcessSkin() {
        return false;
    }

    @Override
    protected DataStoreKey getCacheKey(NameInfo info) {
        return DataStoreKey.forCape(info);
    }


    @Override
    public String getName() {
        return "mojang_cape";
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.CAPE;
    }
}
