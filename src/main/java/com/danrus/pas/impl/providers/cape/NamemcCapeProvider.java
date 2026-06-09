package com.danrus.pas.impl.providers.cape;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class NamemcCapeProvider extends AbstractNamemcProvider<CapeData> {

    @Override
    public String getLiteral() {
        return "A";
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() { return CapeData.class; }

    @Override
    protected String getNamemcId(NameInfo info) { return info.getFeature(CapeFeature.class).getId(); }

    @Override
    protected boolean shouldRemap() { return false; }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }

    @Override
    protected CapeData createDataHolder() {
        return new CapeData();
    }

    @Override
    protected void updateSkinData(NameInfo info, ResourceLocation texture) {
        CapeData data = this.getOrCreateDataHolder(info);
        data.setTexture(texture);
        data.setStatus(DownloadStatus.COMPLETED);
        getDataManager().store(info, data);
    }

    @Override
    protected Optional<CapeData> getDataFromNamemcRepository(NameInfo info) {
        return PasManager.getInstance().getCapeDataManager().getSource("namemc_cape").get(info);
    }

    @Override
    protected String getOutputString(NameInfo info) {
        return info.getFeature(CapeFeature.class).compile();
    }
}
