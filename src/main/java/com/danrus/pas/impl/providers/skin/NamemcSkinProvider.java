package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.SkinDownloader;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NamemcSkinProvider extends AbstractNamemcProvider<SkinData> {
    @Override
    protected CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info) {
        ResourceLocation location = InfoTranslators.getInstance()
                .toResourceLocation(SkinData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(SkinData.class, info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");
        AbstractDiskDataProvider.AGES.touch(fileName + ".png");
        return SkinDownloader.downloadAndRegister(
                location,
                filePath,
                "https://s.namemc.com/i/" + info.base() + ".png",
                true
        );
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }


    @Override
    protected void updateSkinData(NameInfo info, ResourceLocation texture) {
        SkinData data = getDataManager().findData(info);
        data.setTexture(texture);
        getDataManager().store(info, data);
    }

    @Override
    protected String getOutputString(NameInfo info) {
        return info.base();
    }
}