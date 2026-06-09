package com.danrus.pas.managers;

import com.danrus.pas.api.data.DataType;
import com.danrus.pas.api.data.TextureProvidersManager;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDataRepository;
import com.danrus.pas.impl.data.common.DiskDataProvider;
import com.danrus.pas.impl.data.skin.ClientLevelSkinData;
import com.danrus.pas.impl.data.skin.FileTextureSkinData;
import com.danrus.pas.impl.holder.SkinData;

public class SkinDataRepository extends AbstractDataRepository<SkinData> {
    @Override
    protected void prepareSources() {
        addSource(new ClientLevelSkinData(), 0);
        addSource(new DiskDataProvider<>(
                "mojang_skin", DataType.SKIN, SkinData.class,
                SkinData::new, () -> PasManager.getInstance().getSkinDataManager(),
                true, null));
        addSource(new DiskDataProvider<>(
                "namemc", DataType.SKIN, SkinData.class,
                SkinData::new, () -> PasManager.getInstance().getSkinDataManager(),
                true, null));
        addSource(new FileTextureSkinData(), 999);
    }

    @Override
    protected SkinData createData() {
        return new SkinData();
    }

    @Override
    protected TextureProvidersManager getTextureProvidersManager() {
        return PasManager.getInstance().getSkinProviderManager();
    }
}
