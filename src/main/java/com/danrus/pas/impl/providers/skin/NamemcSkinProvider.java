package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.data.common.AbstractDiskDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.SkinDownloader;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NamemcSkinProvider extends AbstractNamemcProvider<SkinData> {
    @Override
    protected CompletableFuture<Identifier> getDownloadTask(NameInfo info) {
        Identifier location = InfoTranslators.getInstance()
                .toIdentifier(SkinData.class, info);
        String fileName = InfoTranslators.getInstance()
                .toFileName(SkinData.class, info);
        Path filePath = AbstractDiskDataProvider.CACHE_PATH.resolve(fileName + ".png");
        return SkinDownloader.downloadAndRegister(
                location,
                filePath,
                "https://s.namemc.com/i/" + info.base() + ".png",
                true
        );
    }

    @Override
    protected DataRepository getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected SkinData createDataHolder(NameInfo info) {
        return new SkinData(info);
    }

    @Override
    protected void updateSkinData(NameInfo info, Identifier texture) {
        SkinData data = this.getOrCreateDataHolder(info);
        data.setTexture(texture);
        getDataManager().store(info, data);
    }

    @Override
    protected SkinData getDataFromNamemcRepository(NameInfo info) {
        return PasManager.getInstance().getSkinDataManager().getSource("namemc").get(info);
    }

    @Override
    protected String getOutputString(NameInfo info) {
        return info.base();
    }
}