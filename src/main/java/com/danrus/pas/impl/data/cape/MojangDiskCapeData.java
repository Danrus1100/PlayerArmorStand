package com.danrus.pas.impl.data.cape;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataRepository;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;

public class MojangDiskCapeData extends AbstractDiskDataProvider<CapeData> {

    @Override
    public CapeData get(NameInfo info) {
        if (info.getFeature(CapeFeature.class).getProvider().isEmpty()) {
            return super.get(info);
        }
        return null;
    }

    @Override
    protected CapeData createDataHolder(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() {
        return CapeData.class;
    }

    @Override
    protected boolean shouldProcessSkin() {
        return false;
    }

    @Override
    protected String getKeySuffix() {
        return "_cape";
    }

    @Override
    public String getName() {
        return "mojang_cape";
    }
}
