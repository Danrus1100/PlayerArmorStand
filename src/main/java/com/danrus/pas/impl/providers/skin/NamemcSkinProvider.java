package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.api.*;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.common.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.impl.data.skin.NamemcDiskData;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class NamemcSkinProvider extends AbstractNamemcProvider<SkinData> {
    @Override
    protected CompletableFuture<ResourceLocation> getDownloadTask(NameInfo info) {
        return SkinDownloader.downloadAndRegister(
                Rl.pas( "skins/" + info.base()),
                NamemcDiskData.CACHE_PATH.resolve(info.base() + "_namemc.png"),
                "https://s.namemc.com/i/" + info.base() + ".png",
                true
        );
    }

    @Override
    protected DataRepository getDataManager() {
        return PasManager.getInstance().getDataManager();
    }
}