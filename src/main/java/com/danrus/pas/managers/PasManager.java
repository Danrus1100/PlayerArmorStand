package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.utils.TextureUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PasManager {

    private final Logger LOGGER = LoggerFactory.getLogger("PasManager");

    private static PasManager INSTANCE;

    private SkinDataRepository skinDataRepository;
    private CapeDataRepository capeDataRepository;
    private SkinProvidersManager skinProviderManager;
    private CapeProvidersManager capeProviderManager;
    private List<String> existingProviders;

    private PasManager() {
        // Инициализируем список провайдеров первым
        existingProviders = new ArrayList<>(List.of("F"));

        // Создаем репозитории
        skinDataRepository = new SkinDataRepository();
        capeDataRepository = new CapeDataRepository();

        // Создаем менеджеры провайдеров
        skinProviderManager = new SkinProvidersManager();
        capeProviderManager = new CapeProvidersManager();

        // Инициализируем провайдеры, передавая ссылку на себя
        skinProviderManager.initialize(this);
        capeProviderManager.initialize(this);
    }

    public ResourceLocation getSkinWithOverlayTexture(NameInfo info) {
        return TextureUtils.getOverlayedTexture(info, SkinData.class);
    }

    public ResourceLocation getCapeWithOverlayTexture(NameInfo info) {
        return TextureUtils.getOverlayedTexture(info, CapeData.class);
    }

    public ResourceLocation getSkinTexture(NameInfo info) {
        SkinData data = skinDataRepository.getData(info);
        return data != null ? data.getTexture(info) : null;
    }

    public ResourceLocation getCapeTexture(NameInfo info) {
        CapeData data = capeDataRepository.getData(info);
        return data != null ? data.getTexture(info) : null;
    }

    public SkinData findSkinData(NameInfo info) {
        if (info.isEmpty()) {
            return null;
        }
        return getSkinDataManager().findData(info);
    }

    public CapeData findCapeData(NameInfo info) {
        if (info.isEmpty()) {
            return null;
        }
        return getCapeDataManager().findData(info);
    }

    public void dropCache() {

        existingProviders = new ArrayList<>(List.of("F"));
        skinDataRepository = new SkinDataRepository();
        capeDataRepository = new CapeDataRepository();
        skinProviderManager = new SkinProvidersManager();
        capeProviderManager = new CapeProvidersManager();

        skinProviderManager.initialize(this);
        capeProviderManager.initialize(this);

        PlayerArmorStandsClient.LOGGER.info("PasManager: Dropped all cached data");
    }

    public void reloadData(String string){
        NameInfo info = NameInfo.parse(string);
        getSkinDataManager().delete(info);
        getCapeDataManager().delete(info);
        TextureUtils.clearOverlayCacheFor(string);

        if (info.isEmpty()) {
            this.LOGGER.warn("Cannot reload data for an empty name");
            return;
        }

        if (skinDataRepository.getData(info) == null) {
            this.LOGGER.warn("No data found for " + info.base() + ", reloading from skin providers");
            return;
        }

        if (capeDataRepository.getData(info) == null) {
            this.LOGGER.warn("No data found for " + info.base() + ", reloading from cape providers");
            return;
        }
    }

    public void reloadFailed() {
        this.LOGGER.info("Reloading failed textures");

        // Перезагрузка failed скинов
        skinDataRepository.getSources().forEach((key, source) -> {
            source.getAll().forEach((dataKey, data) -> {
                if (data.getStatus() == DownloadStatus.FAILED) {
                    this.LOGGER.info("Reloading failed skin for " + dataKey);
                    data.setStatus(DownloadStatus.NOT_STARTED);
                    skinProviderManager.download(dataKey);
                }
            });
        });

        // Перезагрузка failed плащей
        capeDataRepository.getSources().forEach((key, source) -> {
            source.getAll().forEach((dataKey, data) -> {
                if (data.getStatus() == DownloadStatus.FAILED) {
                    this.LOGGER.info("Reloading failed cape for " + dataKey);
                    data.setStatus(DownloadStatus.NOT_STARTED);
                    capeProviderManager.download(dataKey);
                }
            });
        });
    }

    public static PasManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PasManager();
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
