package com.danrus.pas.impl.data.cape;

import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractCacheDataProvider;
import com.danrus.pas.impl.holder.CapeData;

public class CacheCapeData extends AbstractCacheDataProvider<CapeData> {

    @Override
    protected CapeData createDataHolder(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    protected DataStoreKey getKey(NameInfo info) {
        return DataStoreKey.forCape(info);
    }

    @Override
    public String getName() {
        return "game";
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.CAPE;
    }
}
