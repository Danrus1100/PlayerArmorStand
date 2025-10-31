package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.skin.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SkinDataManager implements DataRepository {
    private final List<DataProvider<?>> sources = new CopyOnWriteArrayList<>();


    public SkinDataManager() {
        addSource(new ClientLevelData(), 0);
        addSource(new GameData(), 2);
        addSource(new MojangDiskData());
        addSource(new NamemcDiskData());
        addSource(new FileTextureData(), 999);
    }

    @Override
    public void addSource(DataProvider<?> source) {
        sources.add(source);
    }

    @Override
    public void addSource(DataProvider<?> source, int priority) {
        if (priority >= sources.size()) {
            sources.add(source);
        } else {
            sources.add(priority, source);
        }
    }

    public DataProvider<?> getSource(String key) {
        return sources.stream()
                .filter(source -> source.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    @Override
    public HashMap<String, LegacySkinData> getGameData() {
        return sources.stream()
                .map(source -> (DataProvider<LegacySkinData>) source)
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
    public LegacySkinData findData(NameInfo info) {
        DataProvider<?> source = getSource("game");
        DataProvider<LegacySkinData> gameCache = source instanceof DataProvider<?> ? (DataProvider<LegacySkinData>) source : null;
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


    public HashMap<String, DataProvider<?>> getSources(){
        return sources.stream()
                .collect(Collectors.toMap(
                        DataProvider::getName,
                        source -> source,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    public LegacySkinData getData(NameInfo info) {
        AtomicReference<LegacySkinData> data = new AtomicReference<>(new LegacySkinData(info));
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach(source -> {
            LegacySkinData dataFromSource = needDownload.get() ? source.get(info) : null;
            if (dataFromSource != null) {
                needDownload.set(false);
                data.set(dataFromSource);
            }
        });
        if (needDownload.get() && data.get().getStatus() == DownloadStatus.NOT_STARTED) {
            data.get().setStatus(DownloadStatus.IN_PROGRESS);
            store(info, data.get());
            PasManager.getInstance().getSkinProviderManager().download(info);
        }
        return data.get();
    }

    public void store(NameInfo info, Object data) {
        sources.forEach(source -> {
            if (source.isCompatibleWith(data)) {
                source.store(info, data);
            }
        });
    }

    public void invalidateData(NameInfo info) {
        sources.forEach(source -> source.invalidateData(info));
    }
}
