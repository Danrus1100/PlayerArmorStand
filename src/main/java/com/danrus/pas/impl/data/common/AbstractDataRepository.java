package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.*;
import com.danrus.pas.api.info.NameInfo;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class AbstractDataRepository<T extends DataHolder> implements DataRepository<T> {

    private final List<DataProvider<T>> sources = new CopyOnWriteArrayList<>();

    public AbstractDataRepository(){
        prepareSources();
    }

    @Override
    public void addSource(DataProvider<T> source) {
        sources.add(source);
    }

    @Override
    public void addSource(DataProvider<T> source, int priority) {
        if (priority >= sources.size()) {
            sources.add(source);
        } else {
            sources.add(priority, source);
        }
    }

    @Override
    public T getData(NameInfo info) {
        AtomicReference<T> data = new AtomicReference<>(createData(info));
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach(source -> {
            T dataFromSource = needDownload.get() ? source.get(info) : null;
            if (dataFromSource != null) {
                needDownload.set(false);
                data.set(dataFromSource);
            }
        });
        if (needDownload.get() && data.get().getStatus() == DownloadStatus.NOT_STARTED) {
            data.get().setStatus(DownloadStatus.IN_PROGRESS);
            store(info, data.get());
            getTextureProvidersManager().download(info);
        }
        return data.get();
    }

    @Override
    public void store(NameInfo info, T data) {
        sources.forEach(source -> {
            source.store(info, data);
        });
    }

    @Override
    public void invalidateData(NameInfo info) {
        sources.forEach(source -> source.invalidateData(info));
    }

    @Override
    @Nullable
    public DataProvider<T> getSource(String key) {
        return (DataProvider<T>) sources.stream()
                .filter(source -> source.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    @Override
    public HashMap<DataStoreKey, T> getGameData() {
        return sources.stream()
                .map(source -> (DataProvider<T>) source)
                .map(DataProvider::getAll)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    @Override
    public T findData(NameInfo info) {
        DataProvider<T> source = getSource("game");
        DataProvider<T> gameCache = source instanceof DataProvider<?> ? source : null;
        if (gameCache != null) {
            return gameCache.get(info);
        }
        return null;
    }

    @Override
    public void delete(NameInfo info) {
        sources.forEach(source -> {
            if (source.delete(info)) {
                PlayerArmorStandsClient.LOGGER.info("Deleted data from source: " + source.getName() + " for string: " + info);
            }
        });
    }

    @Override
    public HashMap<String, DataProvider<T>> getSources() {
        return sources.stream()
                .collect(Collectors.toMap(
                        DataProvider::getName,
                        source -> source,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    protected abstract void prepareSources();
    protected abstract T createData(NameInfo info);
    protected abstract TextureProvidersManager getTextureProvidersManager();
}
