package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.data.common.AbstractCacheDataProvider;
import com.danrus.pas.impl.holder.SkinData;

public class CacheSkinData extends AbstractCacheDataProvider<SkinData> {

    @Override
    protected SkinData createDataHolder(NameInfo info) {
        return new SkinData(info);
    }

    @Override
    public String getName() {
        return "game";
    }
}
