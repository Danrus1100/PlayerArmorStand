package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;

public class MojangDiskSkinData extends AbstractDiskDataProvider<SkinData> {

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
    protected DataStoreKey getCacheKey(NameInfo info) {
        return DataStoreKey.forSkin(info);
    }

    @Override
    public String getName() {
        return "mojang_skin";
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.SKIN;
    }
}
