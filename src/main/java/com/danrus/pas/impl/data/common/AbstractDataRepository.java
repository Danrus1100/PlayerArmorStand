package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.api.data.*;
import com.danrus.pas.api.info.NameInfo;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
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
        if (priority < 0) {
            throw new IllegalArgumentException("Priority cannot be negative");
        }
        if (priority >= sources.size()) {
            sources.add(source);
        } else {
            sources.add(priority, source);
        }
    }

    @Override
    public T getData(NameInfo info) {
        T data = createData(info);
        boolean needDownload = true;
        for (DataProvider<T> source : sources) {
            T dataFromSource = needDownload ? source.get(info) : null;
            if (dataFromSource != null) {
                needDownload = false;
                data = dataFromSource;
            }
        }
        if (data.getStatus() == DownloadStatus.NOT_STARTED) {
            data.setStatus(DownloadStatus.IN_PROGRESS);
            store(info, data);
            getTextureProvidersManager().download(info);
        }
        return data;
    }

    @Override
    public void store(NameInfo info, T data) {
        sources.forEach(source -> {
            source.store(info, data);
        });
    }

    @Override
    public void store(DataStoreKey key, T data) {
        sources.forEach(source -> {
            source.store(key, data);
        });
    }

    @Override
    public void invalidateData(NameInfo info) {
        sources.forEach(source -> source.invalidateData(info));
    }

    @Override
    public void invalidateData(DataStoreKey key) {
        sources.forEach(source -> source.invalidateData(key.tryToNameInfo()));
    }

    @Override
    @Nullable
    public DataProvider<T> getSource(String key) {
                return sources.stream()
                .filter(source -> source.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    private <R> R doWithGameData(Function<DataProvider<T>, R> function) {
        DataProvider<T> source = getSource("game");
        if (source != null) {
            return function.apply(source);
        }
        return null;
    }

    @Override
    public HashMap<DataStoreKey, T> getGameData() {
        return doWithGameData(DataProvider::getAll);
    }

    @Override
    public T findData(NameInfo info) {
        return doWithGameData(source -> source.get(info));
    }

    @Override
    public T findData(DataStoreKey key) {
        return doWithGameData(source -> source.get(key));
    }

    @Override
    public void delete(NameInfo info) {
        sources.forEach(source -> {
            if (source.delete(info)) {
                PlayerArmorStandsClient.LOGGER.info("Deleted data from source: {} for name info: {}", source.getName(), info);
            }
        });
    }

    @Override
    public void delete(DataStoreKey key) {
        sources.forEach(source -> {
            if (source.delete(key)) PlayerArmorStandsClient.LOGGER.info("Deleted data from source: {} for key: {}", source.getName(), key);
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
