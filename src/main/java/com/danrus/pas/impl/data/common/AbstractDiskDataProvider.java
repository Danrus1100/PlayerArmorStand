package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataProvider;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.utils.ModUtils;
import com.danrus.pas.utils.TextureUtils;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractDiskDataProvider<T extends DataHolder> implements DataProvider<T> {

    public static final Path CACHE_PATH = ModUtils.getGameDir().resolve("cache/pas");

    protected final Path cachePath;
    protected final HashMap<DataStoreKey, ResourceLocation> cache = new HashMap<>();

    public AbstractDiskDataProvider() {
        this.cachePath = CACHE_PATH;
        if (!cachePath.toFile().exists()) {
            cachePath.toFile().mkdirs();
        }
    }

    @Override
    public T get(NameInfo info) {
        if (getDataManager().findData(info) != null && getDataManager().findData(info).getStatus() == DownloadStatus.IMPOSSIBLE_TO_DOWNLOAD) {
            return null;
        }

        String fileName = InfoTranslators.getInstance()
                .toFileName(getDataHolderClass(), info) + ".png";
        Path filePath = cachePath.resolve(fileName);

        if (!filePath.toFile().exists()) {
            return null;
        }

        ResourceLocation texture = InfoTranslators.getInstance()
                .toResourceLocation(getDataHolderClass(), info);

        TextureUtils.registerTexture(filePath, texture, shouldProcessSkin());
        cache.put(getCacheKey(info), texture);

        T data = createDataHolder(info);
        data.setTexture(texture);
        data.setStatus(DownloadStatus.COMPLETED);

        getDataManager().store(info, data);
        return data;
    }

    @Override
    public T get(DataStoreKey key) {
        // Not implemented
        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        String fileName = InfoTranslators.getInstance()
                .toFileName(getDataHolderClass(), info);
        Path filePath = cachePath.resolve(fileName);

        boolean deleted = false;
        if (filePath.toFile().exists()) {
            deleted = filePath.toFile().delete();
        }

        if (deleted) {
            cache.remove(getCacheKey(info));
        }

        return deleted;
    }

    @Override
    public HashMap<DataStoreKey, T> getAll() {
        return new HashMap<>();
    }

    @Override
    public void store(NameInfo info, T data) {
        // NO-OP - disk провайдер только читает из кеша
    }

    @Override
    public void invalidateData(NameInfo info) {
        String fileName = InfoTranslators.getInstance()
                .toFileName(getDataHolderClass(), info);
        Path filePath = cachePath.resolve(fileName);

        if (filePath.toFile().exists()) {
            filePath.toFile().delete();
        }
        cache.remove(getCacheKey(info));
    }

    private List<Path> getCacheFiles() {
        try {
            return List.of(cachePath.toFile().listFiles()).stream()
                    .filter(file -> file.isFile() && file.getName().endsWith(".png"))
                    .map(file -> file.toPath())
                    .toList();
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.error("Error listing cache files in " + cachePath, e);
            return List.of();
        }
    }

    protected abstract T createDataHolder(NameInfo info);
    protected abstract DataRepository<T> getDataManager();
    protected abstract Class<? extends DataHolder> getDataHolderClass();
    protected abstract boolean shouldProcessSkin();
    protected abstract DataStoreKey getCacheKey(NameInfo info);
}
