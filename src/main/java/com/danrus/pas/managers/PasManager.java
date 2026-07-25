package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.TextureProvidersManager;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.impl.holder.AbstractPasHolder;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.utils.texture.TextureUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class PasManager {

    private final Logger LOGGER = LoggerFactory.getLogger("PasManager");

    private static volatile PasManager INSTANCE;

    private volatile SkinDataRepository skinDataRepository;
    private volatile CapeDataRepository capeDataRepository;
    private volatile SkinProvidersManager skinProviderManager;
    private volatile CapeProvidersManager capeProviderManager;
    private List<String> existingProviders;

    private PasManager() {
        existingProviders = new CopyOnWriteArrayList<>(List.of("F"));

        skinDataRepository = new SkinDataRepository();
        capeDataRepository = new CapeDataRepository();

        skinProviderManager = new SkinProvidersManager();
        capeProviderManager = new CapeProvidersManager();

        skinProviderManager.initialize(this);
        capeProviderManager.initialize(this);
    }

    public ResourceLocation getSkinWithOverlayTexture(NameInfo info) {
        if (info.isEmpty()) return SkinData.DEFAULT_TEXTURE;
        return TextureUtils.getOverlayedTexture(info, SkinData.class);
    }

    public ResourceLocation getCapeWithOverlayTexture(NameInfo info) {
        if (info.isEmpty()) return CapeData.DEFAULT_TEXTURE;
        return TextureUtils.getOverlayedTexture(info, CapeData.class);
    }

    public ResourceLocation getSkinTexture(NameInfo info) {
        Optional<SkinData> data = skinDataRepository.getData(info);
        return data.map(AbstractPasHolder::getTexture).orElse(SkinData.DEFAULT_TEXTURE);
    }

    public ResourceLocation getCapeTexture(NameInfo info) {
        Optional<CapeData> data = capeDataRepository.getData(info);
        return data.map(AbstractPasHolder::getTexture).orElse(CapeData.DEFAULT_TEXTURE);
    }

    public Optional<SkinData> findSkinData(NameInfoLike info) {
        return getSkinDataManager().findFirst(info);
    }

    public Optional<CapeData> findCapeData(NameInfoLike info) {
        return getCapeDataManager().findFirst(info);
    }

    public void dropCache() {
        TextureUtils.clearOverlayCache();
        skinDataRepository.clear();
        capeDataRepository.clear();
        skinProviderManager.clearPending();
        capeProviderManager.clearPending();
        PlayerArmorStandsClient.LOGGER.info("PasManager: Dropped all cached data");
    }

    public void reloadDataLike(NameInfoLike info, Class<? extends DataHolder> type) {
        if (type == SkinData.class) {
            reloadDataLike(info, skinDataRepository, getSkinProviderManager(), "skin");
        } else if (type == CapeData.class) {
            reloadDataLike(info, capeDataRepository, getCapeProviderManager(), "cape");
        } else {
            this.LOGGER.warn("Unknown data type for reload: " + type.getSimpleName());
        }
    }

    private <T extends DataHolder> void reloadDataLike(NameInfoLike infoLike, DataRepository<T> repository, TextureProvidersManager provider, String type) {
        Map<NameInfo, T> dataCollection = repository.findAll(infoLike);
        repository.deleteAllOf(infoLike);
        dataCollection.forEach((info, data) -> {
            TextureUtils.unregisterTexture(data.getTexture());
            TextureUtils.clearOverlayCacheFor(info);
            if (repository.getData(info).isEmpty()) {
                this.LOGGER.warn("No data found for " + info.base() + ", reloading from " + type + " providers");
            }
            provider.download(info);
        });
    }

    public void reloadFailed() {
        this.LOGGER.info("Reloading failed textures");

        reloadFailed(skinDataRepository, skinProviderManager, "skin");
        reloadFailed(capeDataRepository, capeProviderManager, "cape");
    }

    private <T extends DataHolder> void reloadFailed(DataRepository<T> repository, TextureProvidersManager provider, String type) {
        repository.getSources().forEach((key, source) -> {
            source.getAll().forEach((info, data) -> {
                if (data.compareAndSetStatus(DownloadStatus.FAILED, DownloadStatus.NOT_STARTED)) {
                    this.LOGGER.info("Reloading failed {} for {}", type, info);
                    provider.download(info);
                }
            });
        });
    }

    public static PasManager getInstance() {
        if (INSTANCE == null) {
            synchronized (PasManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PasManager();
                }
            }
        }
        return INSTANCE;
    }

    public DataRepository<SkinData> getSkinDataManager() {
        return skinDataRepository;
    }

    public DataRepository<CapeData> getCapeDataManager() {
        return capeDataRepository;
    }

    public TextureProvidersManager getSkinProviderManager() {
        return skinProviderManager;
    }

    public TextureProvidersManager getCapeProviderManager() {
        return capeProviderManager;
    }

    public List<String> getExistingProviders() {
        return existingProviders;
    }
}
