package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class NamemcSkinProvider extends AbstractNamemcProvider<SkinData> {
    @Override
    protected Class<? extends DataHolder> getDataHolderClass() { return SkinData.class; }

    @Override
    protected String getNamemcId(NameInfo info) { return info.base(); }

    @Override
    protected boolean shouldRemap() { return true; }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected SkinData createDataHolder() {
        return new SkinData();
    }

    @Override
    protected void updateSkinData(NameInfo info, ResourceLocation texture) {
        SkinData data = this.getOrCreateDataHolder(info);
        data.setTexture(texture);
        getDataManager().store(info, data);
    }

    @Override
    protected Optional<SkinData> getDataFromNamemcRepository(NameInfo info) {
        return PasManager.getInstance().getSkinDataManager().getSource("namemc").get(info);
    }

    @Override
    protected String getOutputString(NameInfo info) {
        return info.base();
    }
}