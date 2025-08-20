package com.danrus.pas.utils.data;

import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.DataCache;

import java.util.HashMap;

public class GameCache implements DataCache<SkinData> {

    private HashMap<String, SkinData> cache = new HashMap<>();

    @Override
    public SkinData get(String name) {
        if (!cache.containsKey(name)) {
            return null;
        }
        return cache.get(name);
    }

    @Override
    public boolean delete(String string) {
        if (cache.containsKey(string)) {
            cache.remove(string);
            return true;
        }
        return false;
    }

    @Override
    public HashMap<String, SkinData> getAll() {
        return cache;
    }

    @Override
    public void store(String name, Object data) {
        cache.put(name, (SkinData) data);
    }

    @Override
    public void invalidateData(String name) {
        if (cache.containsKey(name)) {
            cache.get(name).setStatus(DownloadStatus.FAILED);
        } else {
            SkinData data = new SkinData(name);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(name, data);
        }
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof SkinData;
    }

    @Override
    public String getName() {
        return "game";
    }
}
