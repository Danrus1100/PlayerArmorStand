package com.danrus.pas.impl.providers.skin;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.providers.AbstractNamemcProvider;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.SkinDownloader;
import com.danrus.pas.impl.data.NamemcDiskData;
import com.danrus.pas.managers.OverlayMessageManger;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NamemcSkinProvider extends AbstractNamemcProvider {
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
    protected DataManager getDataManager() {
        return PasManager.getInstance().getDataManager();
    }
}