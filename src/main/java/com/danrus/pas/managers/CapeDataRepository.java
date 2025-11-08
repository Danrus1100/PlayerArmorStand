package com.danrus.pas.managers;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.TextureProvidersManager;
import com.danrus.pas.impl.data.cape.CacheCapeData;
import com.danrus.pas.impl.data.cape.ClientLevelCapeData;
import com.danrus.pas.impl.data.cape.MojangDiskCapeData;
import com.danrus.pas.impl.data.cape.NamemcDiskCapeData;
import com.danrus.pas.impl.data.common.AbstractDataRepository;
import com.danrus.pas.impl.holder.CapeData;

public class CapeDataRepository extends AbstractDataRepository<CapeData> {

    @Override
    protected void prepareSources() {
        addSource(new ClientLevelCapeData(), 0);
        addSource(new CacheCapeData(), 2);
        addSource(new MojangDiskCapeData());
        addSource(new NamemcDiskCapeData());
    }

    @Override
    protected CapeData createData(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    protected TextureProvidersManager getTextureProvidersManager() {
        return PasManager.getInstance().getCapeProviderManager();
    }
}
