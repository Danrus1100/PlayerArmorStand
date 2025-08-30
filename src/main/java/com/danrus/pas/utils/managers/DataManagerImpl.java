package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DataManager;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.DataCache;
import com.danrus.pas.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DataManagerImpl implements DataManager {
    private final List<DataCache<?>> sources = new CopyOnWriteArrayList<>();

    @Override
    public void addSource(DataCache<?> source) {
        sources.add(source);
    }

    @Override
    public void addSource(DataCache<?> source, int priority) {
        if (priority >= sources.size()) {
            sources.add(source);
        } else {
            sources.add(priority, source);
        }
    }

    public DataCache<?> getSource(String key) {
        return sources.stream()
                .filter(source -> source.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    @Override
    public HashMap<String, SkinData> getGameData() {
        return sources.stream()
                .map(source -> (DataCache<SkinData>) source) // FIXME: unsafe
                .map(DataCache::getAll)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    @Override
    public SkinData findData(String name) {
        DataCache<SkinData> gameCache = getSource("game") instanceof DataCache<?> source ? (DataCache<SkinData>) source : null;
        if (gameCache != null) {
            return gameCache.get(name);
        }
        return null;
    }

    @Override
    public void delete(String string) {
        sources.forEach(source -> {
            if (source.delete(string)) {
                PlayerArmorStandsClient.LOGGER.info("Deleted data from source: " + source.getName() + " for string: " + string);
            }
        });
    }


    public HashMap<String, DataCache<?>> getSources(){
        return sources.stream()
                .collect(Collectors.toMap(
                        DataCache::getName,
                        source -> source,
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    public SkinData getData(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        AtomicReference<SkinData> data = new AtomicReference<>(new SkinData(name));
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach(source -> {
            SkinData dataFromSource = needDownload.get() ? source.get(string) : null;
            if (dataFromSource != null) {
                needDownload.set(false);
                data.set(dataFromSource);
            }
        });
        if (needDownload.get() && data.get().getStatus() == DownloadStatus.NOT_STARTED) {
            data.get().setStatus(DownloadStatus.IN_PROGRESS);
            store(name, data.get());
            SkinManger.getInstance().getSkinProviderManager().download(string);
        }
        return data.get();
    }

    public void store(String name, Object data) {
        sources.forEach(source -> {
            if (source.isCompatibleWith(data)) {
                source.store(name, data);
            }
        });
    }

    public void invalidateData(String name) {
        sources.forEach(source -> source.invalidateData(name));
    }
}
