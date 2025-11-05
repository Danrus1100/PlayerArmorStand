package com.danrus.pas.managers;

import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.common.AbstractDataRepository;
import com.danrus.pas.impl.data.skin.*;
import com.danrus.pas.impl.holder.SkinData;

import java.util.HashMap;

public class SkinDataRepository extends AbstractDataRepository<SkinData> {
    @Override
    protected void prepareSources() {
        addSource(new ClientLevelSkinData(), 0);
        addSource(new CacheSkinData(), 2);
        addSource(new MojangDiskSkinData());
        addSource(new NamemcDiskSkinData());
        addSource(new FileTextureSkinData(), 999);
    }

    @Override
    protected SkinData createData(NameInfo info) {
        return new SkinData(info);
    }
}
