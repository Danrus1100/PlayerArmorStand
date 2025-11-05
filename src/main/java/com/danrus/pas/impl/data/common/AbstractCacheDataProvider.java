package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;

import java.util.HashMap;

public abstract class AbstractCacheDataProvider<T extends DataHolder> implements DataProvider<T> {

    protected final HashMap<String, T> cache = new HashMap<>();

    @Override
    public T get(NameInfo info) {
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
    public HashMap<String, T> getAll() {
        return cache;
    }

    @Override
    public void store(NameInfo info, T data) {
        cache.put(info.base(), data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        if (cache.containsKey(info.base())) {
            cache.get(info.base()).setStatus(DownloadStatus.FAILED);
        } else {
            T data = createDataHolder(info);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(info.base(), data);
        }
    }

    protected abstract T createDataHolder(NameInfo info);
}
