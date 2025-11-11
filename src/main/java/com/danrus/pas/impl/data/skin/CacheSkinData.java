package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractCacheDataProvider;
import com.danrus.pas.impl.holder.SkinData;

public class CacheSkinData extends AbstractCacheDataProvider<SkinData> {

    @Override
    protected SkinData createDataHolder(NameInfo info) {
        return new SkinData(info);
    }

    @Override
    protected DataStoreKey getKey(NameInfo info) {
        return DataStoreKey.forSkin(info);
    }

    @Override
    public String getName() {
        return "game";
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.SKIN;
    }
}
