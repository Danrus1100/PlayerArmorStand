package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.LegacySkinData;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.DataProvider;

import java.util.HashMap;

public class GameData implements DataProvider<LegacySkinData> {

    private HashMap<String, LegacySkinData> cache = new HashMap<>();

    @Override
    public LegacySkinData get(NameInfo info) {
        if (!cache.containsKey(info.base())) {
            return null;
        }
        return cache.get(info.base());
    }

    @Override
    public boolean delete(NameInfo info) {
        if (cache.containsKey(info.base())) {
            cache.remove(info.base());
            return true;
        }
        return false;
    }

    @Override
    public HashMap<String, LegacySkinData> getAll() {
        return cache;
    }

    @Override
    public void store(NameInfo info, Object data) {
        cache.put(info.base(), (LegacySkinData) data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        if (cache.containsKey(info.base())) {
            cache.get(info.base()).setStatus(DownloadStatus.FAILED);
        } else {
            LegacySkinData data = new LegacySkinData(info);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(info.base(), data);
        }
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof LegacySkinData;
    }

    @Override
    public String getName() {
        return "game";
    }
}
