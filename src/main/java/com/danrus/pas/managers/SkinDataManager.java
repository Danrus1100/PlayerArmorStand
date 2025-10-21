package com.danrus.pas.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.*;
import com.danrus.pas.impl.data.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SkinDataManager implements DataManager {
    private final List<DataHolder<?>> sources = new CopyOnWriteArrayList<>();


    public SkinDataManager() {
        addSource(new ClientLevelData(), 0);
        addSource(new GameData(), 2);
        addSource(new MojangDiskData());
        addSource(new NamemcDiskData());
        addSource(new FileTextureData(), 999);
    }

    @Override
    public void addSource(DataHolder<?> source) {
        sources.add(source);
    }

    @Override
    public void addSource(DataHolder<?> source, int priority) {
        if (priority >= sources.size()) {
            sources.add(source);
        } else {
            sources.add(priority, source);
        }
    }

    public DataHolder<?> getSource(String key) {
        return sources.stream()
                .filter(source -> source.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    @Override
    public HashMap<String, SkinData> getGameData() {
        return sources.stream()
                .map(source -> (DataHolder<SkinData>) source)
                .map(DataHolder::getAll)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    @Override
    public SkinData findData(NameInfo info) {
        DataHolder<?> source = getSource("game");
        DataHolder<SkinData> gameCache = source instanceof DataHolder<?> ? (DataHolder<SkinData>) source : null;
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


    public HashMap<String, DataHolder<?>> getSources(){
        return sources.stream()
                .collect(Collectors.toMap(
                        DataHolder::getName,
                        source -> source,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    public SkinData getData(NameInfo info) {
        AtomicReference<SkinData> data = new AtomicReference<>(new SkinData(info));
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach(source -> {
            SkinData dataFromSource = needDownload.get() ? source.get(info) : null;
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
