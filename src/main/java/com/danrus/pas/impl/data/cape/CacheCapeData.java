package com.danrus.pas.impl.data.cape;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.data.common.AbstractCacheDataProvider;
import com.danrus.pas.impl.holder.CapeData;

public class CacheCapeData extends AbstractCacheDataProvider<CapeData> {

    @Override
    protected CapeData createDataHolder(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    public String getName() {
        return "game";
    }
}
