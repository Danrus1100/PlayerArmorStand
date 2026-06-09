package com.danrus.pas.managers;

import com.danrus.pas.api.data.DataType;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.data.TextureProvidersManager;
import com.danrus.pas.impl.data.cape.ClientLevelCapeData;
import com.danrus.pas.impl.data.common.AbstractDataRepository;
import com.danrus.pas.impl.data.common.DiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;

public class CapeDataRepository extends AbstractDataRepository<CapeData> {

    @Override
    protected void prepareSources() {
        addSource(new ClientLevelCapeData(), 0);
        addSource(new DiskDataProvider<>(
                "mojang_cape", DataType.CAPE, CapeData.class,
                CapeData::new, () -> PasManager.getInstance().getCapeDataManager(),
                false,
                info -> "M".equals(info.getFeature(CapeFeature.class).getProvider())));
        addSource(new DiskDataProvider<>(
                "namemc_cape", DataType.CAPE, CapeData.class,
                CapeData::new, () -> PasManager.getInstance().getCapeDataManager(),
                false, null));
        addSource(new DiskDataProvider<>(
                "mccapes", DataType.CAPE, CapeData.class,
                CapeData::new, () -> PasManager.getInstance().getCapeDataManager(),
                false, null));
    }

    @Override
    protected CapeData createData() {
        return new CapeData();
    }

    @Override
    protected TextureProvidersManager getTextureProvidersManager() {
        return PasManager.getInstance().getCapeProviderManager();
    }
}
