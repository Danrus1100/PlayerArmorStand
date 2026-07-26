package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataProvider;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.utils.files.FilesAges;
import com.danrus.pas.utils.info.NameInfoMap;
import com.danrus.pas.utils.mc.ModUtils;
import com.danrus.pas.utils.texture.TextureUtils;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractDiskDataProvider<T extends DataHolder> implements DataProvider<T> {

    public static final Path CACHE_PATH = ModUtils.getGameDir().resolve("cache/pas");
    public static final FilesAges AGES = new FilesAges(CACHE_PATH.resolve("files_ages.json"));

    protected final Path cachePath;
    protected final NameInfoMap<T> cache = new NameInfoMap<>(new ConcurrentHashMap<>());

    public AbstractDiskDataProvider() {
        this.cachePath = CACHE_PATH;
        if (!cachePath.toFile().exists()) {
            cachePath.toFile().mkdirs();
        }
    }

    @Override
    public void clearCache() { cache.clear(); }

    @Override
    public Optional<T> get(NameInfo info) {
        String fileName = InfoTranslators.getInstance()
                .toFileName(getDataHolderClass(), info) + ".png";
        Path filePath = cachePath.resolve(fileName);

        if (!filePath.toFile().exists()) {
            return Optional.empty();
        }

        if (AGES.isExpired(fileName, FilesAges.millisFromSkinReloadTime(PasConfig.getInstance().getSkinReloadTime()))) {
            filePath.toFile().delete();
            cache.remove(info);
            return Optional.empty();
        }

        Identifier texture = InfoTranslators.getInstance().toIdentifier(getDataHolderClass(), info);

        TextureUtils.registerTexture(filePath, texture, shouldProcessSkin());

        T data = createDataHolder();
        data.setTexture(texture);
        data.setStatus(DownloadStatus.COMPLETED);

        cache.put(info, data);

        getDataManager().store(info, data);
        return Optional.of(data);
    }

    @Override
    public Optional<T> findFirst(NameInfoLike infoLike) {
        var entry = cache.findFirst(infoLike);
        if (entry.isEmpty() && infoLike instanceof NameInfo info) {
            return get(info);
        }
        return entry.map(Map.Entry::getValue);
    }

    @Override
    public Collection<T> findAll(NameInfoLike infoLike) {
        return cache.findAll(infoLike).values();
    }

    @Override
    public Optional<T> peek(NameInfo info) {
        return cache.findFirst(info).map(Map.Entry::getValue);
    }

    @Override
    public boolean deleteAllOf(NameInfoLike infoLike) {
        forEachLike(infoLike, info -> {
            String fileName = InfoTranslators.getInstance()
                    .toFileName(getDataHolderClass(), info);
            Path filePath = cachePath.resolve(fileName + ".png");

            boolean deleted = false;
            if (filePath.toFile().exists()) {
                deleted = filePath.toFile().delete();
            }

            if (deleted) {
                cache.remove(info);
                AGES.remove(fileName);
            }
        });
        return true;
    }

    @Override
    public NameInfoMap<T> getAll() {
        return new NameInfoMap<>(cache);
    }

    @Override
    public void store(NameInfo info, T data) {
        // NO-OP - disk provider only reads from cache
    }

    @Override
    public void invalidateData(NameInfoLike infoLike) {
        forEachLike(infoLike, info -> {
            String fileName = InfoTranslators.getInstance()
                    .toFileName(getDataHolderClass(), info);
            Path filePath = cachePath.resolve(fileName);

            if (filePath.toFile().exists()) {
                filePath.toFile().delete();
            }
            cache.put(info, createInvalid());
        });
    }

    private void forEachLike(NameInfoLike infoLike, Consumer<NameInfo> consumer) {
        for (NameInfo info : cache.findAll(infoLike).keySet()) {
            consumer.accept(info);
        }
    }

    private T createInvalid() {
        T data = createDataHolder();
        data.setStatus(DownloadStatus.INVALID);
        return data;
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

    protected abstract T createDataHolder();
    protected abstract DataRepository<T> getDataManager();
    protected abstract Class<? extends DataHolder> getDataHolderClass();
    protected abstract boolean shouldProcessSkin();
}
