package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;

import java.util.HashMap;

public abstract class AbstractCacheDataProvider<T extends DataHolder> implements DataProvider<T> {

    protected final HashMap<NameInfo, T> cache = new HashMap<>();

    @Override
    public T get(NameInfo info) {
        if (!cache.containsKey(info)) {
            return null;
        }
        return cache.get(info);
    }

    @Override
    public boolean delete(NameInfo info) {
        if (cache.containsKey(info)) {
            cache.remove(info);
            return true;
        }
        return false;
    }

    @Override
    public HashMap<NameInfo, T> getAll() {
        return cache;
    }

    @Override
    public void store(NameInfo info, T data) {
        cache.put(info, data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        if (cache.containsKey(info)) {
            cache.get(info).setStatus(DownloadStatus.FAILED);
        } else {
            T data = createDataHolder(info);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(info, data);
        }
    }

    protected abstract T createDataHolder(NameInfo info);
}
