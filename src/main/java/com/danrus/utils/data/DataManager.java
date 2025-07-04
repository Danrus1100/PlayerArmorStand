package com.danrus.utils.data;

import com.danrus.PASClient;
import com.danrus.PASModelData;
import com.danrus.SkinManger;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.DataCache;
import com.danrus.utils.StringUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataManager {
    private HashMap<String, DataCache> sources = new HashMap<>();

    private void addSource(DataCache source, String key) {
        sources.put(key, source);
    }

    public DataManager() {
        addSource(new GameCache(), "game");
        addSource(new MojangDiskCache(), "mojang");
        addSource(new NamemcDiskCache(), "namemc");
    }

    public DataCache getSource(String key) {
        return sources.get(key);
    }

    public HashMap<String, DataCache> getSources(){
        return sources;
    }

    public PASModelData getData(String string) {
        String name = StringUtils.matchASName(string).get(0);
        PASModelData data = new PASModelData(name);
        AtomicBoolean needDownload = new AtomicBoolean(true);
        sources.forEach((key, source) -> {
            PASModelData dataFromSource = needDownload.get() ? source.get(name) : null;
            if (dataFromSource != null) {
                needDownload.set(false);
                data.setSkinTexture(dataFromSource.getSkinTexture());
                data.setCapeTexture(dataFromSource.getCapeTexture());
                data.setStatus(dataFromSource.getStatus());
            }
        });
        if (needDownload.get() && data.getStatus() == DownloadStatus.NOT_STARTED) {
            SkinManger.getInstance().getSkinProviderManager().download(string);
        }
        return data;
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
