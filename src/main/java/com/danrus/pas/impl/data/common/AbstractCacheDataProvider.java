package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;

import java.util.HashMap;

public abstract class AbstractCacheDataProvider<T extends DataHolder> implements DataProvider<T> {

    protected final HashMap<DataStoreKey, T> cache = new HashMap<>();

    @Override
    public T get(NameInfo info) {
        return get(getKey(info));
    }

    @Override
    public T get(DataStoreKey key) {
        return cache.get(key);
    }

    @Override
    public boolean delete(NameInfo info) {
        return delete(getKey(info));
    }

    @Override
    public boolean delete(DataStoreKey key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public HashMap<DataStoreKey, T> getAll() {
        return new HashMap<>(cache);
    }

    @Override
    public void store(NameInfo info, T data) {
        store(getKey(info), data);
    }

    @Override
    public void store(DataStoreKey key, T data) {
        cache.put(key, data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        invalidateData(getKey(info), info);
    }

    private void invalidateData(DataStoreKey key, NameInfo info) {
        T data = cache.get(key);
        if (data != null) {
            data.setStatus(DownloadStatus.FAILED);
        } else {
            data = createDataHolder(info);
            data.setStatus(DownloadStatus.FAILED);
            cache.put(key, data);
        }
    }

    protected abstract T createDataHolder(NameInfo info);
    protected abstract DataStoreKey getKey(NameInfo info);
}
