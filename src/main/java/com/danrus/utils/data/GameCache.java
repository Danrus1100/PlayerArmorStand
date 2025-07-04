package com.danrus.utils.data;

import com.danrus.PASModelData;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.DataCache;

import java.util.HashMap;

public class GameCache implements DataCache<PASModelData>{

    private HashMap<String, PASModelData> cache = new HashMap<>();

    @Override
    public PASModelData get(String name) {
        if (!cache.containsKey(name)) {
            return null;
        }
        return cache.get(name);
    }

    @Override
    public HashMap<String, PASModelData> getAll() {
        return cache;
    }

    @Override
    public void store(String name, PASModelData data) {
        cache.put(name, data);
    }

    @Override
    public void invalidateData(String name) {
        if (cache.containsKey(name)) {
            cache.get(name).setStatus(DownloadStatus.FAILED);
        } else {
            PASModelData data = new PASModelData(name);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(name, data);
        }
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof PASModelData;
    }
}
