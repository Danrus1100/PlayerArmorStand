package com.danrus.pas.utils.managers;

import com.danrus.pas.api.DataManager;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.DataCache;
import com.danrus.pas.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DataManagerImpl implements DataManager {
    private HashMap<String, DataCache<?>> sources = new HashMap<>();

    @Override
    public void addSource(DataCache<?> source) {
        sources.put(source.getName(), source);
    }

    public DataCache<?> getSource(String key) {
        return sources.get(key);
    }

    public HashMap<String, DataCache<?>> getSources(){
        return sources;
    }

    public SkinData getData(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        AtomicReference<SkinData> data = new AtomicReference<>(new SkinData(name));
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach((key, source) -> {
            SkinData dataFromSource = needDownload.get() ? source.get(name) : null;
            if (dataFromSource != null) {
                needDownload.set(false);
//                data.setSkinTexture(dataFromSource.getSkinTexture(matches.get(2), matches.get(3)));
//                data.setCapeTexture(dataFromSource.getCapeTexture(matches.get(2), matches.get(3)));
//                data.setStatus(dataFromSource.getStatus());
                data.set(dataFromSource);
            }
        });
        if (needDownload.get() && data.get().getStatus() == DownloadStatus.NOT_STARTED) {
            SkinManger.getInstance().getSkinProviderManager().download(string);
        }
        return data.get();
    }

    public void store(String name, Object data) {
        sources.forEach((key, source) -> {
            if (source.isCompatibleWith(data)) {
                source.store(name, data);
            }
        });
    }

    public void invalidateData(String name) {
        sources.forEach((key, source) -> {
            source.invalidateData(name);
        });
    }
}
