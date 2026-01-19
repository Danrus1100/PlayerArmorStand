package com.danrus.pas.impl.providers.cape;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.SkinDownloader;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NamemcCapeProvider extends AbstractNamemcProvider<CapeData> {

    @Override
    public String getLiteral() {
        return "A";
    }

    @Override
    protected CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info) {
        ResourceLocation capeLocation = InfoTranslators.getInstance()
                .toResourceLocation(CapeData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(CapeData.class, info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");
        AbstractDiskDataProvider.AGES.touch(fileName + ".png");
        return SkinDownloader.downloadAndRegister(
                capeLocation,
                filePath,
                "https://s.namemc.com/i/" + info.getFeature(CapeFeature.class).getId() + ".png",
                false
        );
    }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }


    @Override
    protected void updateSkinData(NameInfo info, ResourceLocation texture) {
        CapeData data = getDataManager().findData(info);
        data.setTexture(texture);
        data.setStatus(DownloadStatus.COMPLETED);
        getDataManager().store(info, data);
    }

    @Override
    protected String getOutputString(NameInfo info) {
        return info.getFeature(CapeFeature.class).compile();
    }
}
