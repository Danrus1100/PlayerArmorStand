package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataProvider;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.utils.info.NameInfoMap;
import com.danrus.pas.utils.texture.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractFileTextureDataProvider<T extends DataHolder> implements DataProvider<T> {

    private final NameInfoMap<T> cache = new NameInfoMap<>(new ConcurrentHashMap<>());

    protected final T DEFAULT = createDataHolder(defaultTexture());

    @Override
    public void clearCache() { cache.clear(); }

    @Override
    public Optional<T> get(NameInfo info) {
        Optional<T> holder = tryToFindHolder(info);
        if (holder.isEmpty()) return holder;

        getDataManager().store(info, holder.get());
        return holder;
    }

    private Optional<T> tryToFindHolder(NameInfo info) {
        if (cache.containsKey(info)) {
            return Optional.of(cache.get(info));
        }

        if (!info.getDesiredProvider().equals(getProviderCode())) {
            return Optional.empty();
        }

        if (!isValidName(info.base())) {
            return Optional.empty();
        }

        Path filePath = getFilePath(info);
        Identifier textureLocation = InfoTranslators.getInstance().toIdentifier(getDataHolderClass(), info);

        if (filePath.toFile().exists()) {
            Minecraft.getInstance().execute(() -> {
                TextureUtils.registerTexture(filePath, textureLocation, true);
            });
        }

        T data = createDataHolder(textureLocation);
        data.setStatus(DownloadStatus.COMPLETED);
        return Optional.of(data);
    }

    @Override
    public boolean cancelRedownload(NameInfo info) {
        return tryToFindHolder(info).isPresent();
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

    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 16 && name.matches("[a-zA-Z0-9_]+");
    }

    @Override
    public boolean deleteAllOf(NameInfoLike info) {
        return false;
    }

    @Override
    public NameInfoMap< T> getAll() {
        return cache;
    }

    @Override
    public void store(NameInfo info, T data) {
    }

    @Override
    public void invalidateData(NameInfoLike infoLike) {
        for (NameInfo info : cache.findAll(infoLike).keySet()) {
            cache.put(info, DEFAULT);
        }
    }

    private T createFailed() {
        T data = createDataHolder(defaultTexture());
        data.setStatus(DownloadStatus.FAILED);
        return data;
    }

    private List<Path> getCacheFiles() {
        try {
            return List.of(getCachePath().toFile().listFiles()).stream()
                    .filter(file -> file.isFile() && file.getName().endsWith(".png"))
                    .map(file -> file.toPath())
                    .toList();
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.error("Error listing cache files in " + getCachePath(), e);
            return List.of();
        }
    }

    protected abstract Path getFilePath(NameInfo info);
    protected abstract Path getCachePath();
    protected abstract T createDataHolder(Identifier texture);
    protected abstract DataRepository<T> getDataManager();
    protected abstract String getProviderCode();
    protected abstract Class<? extends DataHolder> getDataHolderClass();
    protected abstract Identifier defaultTexture();
}
