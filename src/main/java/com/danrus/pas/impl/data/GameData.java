package com.danrus.pas.impl.data;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.DataHolder;

import java.util.HashMap;

public class GameData implements DataHolder<SkinData> {

    private HashMap<String, SkinData> cache = new HashMap<>();

    @Override
    public SkinData get(NameInfo info) {
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
    public HashMap<String, SkinData> getAll() {
        return cache;
    }

    @Override
    public void store(NameInfo info, Object data) {
        cache.put(info.base(), (SkinData) data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        if (cache.containsKey(info.base())) {
            cache.get(info.base()).setStatus(DownloadStatus.FAILED);
        } else {
            SkinData data = new SkinData(info);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(info.base(), data);
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
