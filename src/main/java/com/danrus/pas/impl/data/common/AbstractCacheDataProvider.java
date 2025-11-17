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
        DataStoreKey key = getKey(info);
                T value = cache.get(key);
        if (value == null) {
            return null;
        }
        return value;
    }

    @Override
    public T get(DataStoreKey key) {
        return cache.get(key);
    }

    @Override
    public boolean delete(NameInfo info) {
        DataStoreKey key = getKey(info);
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
        cache.put(getKey(info), data);
    }

    @Override
    public void invalidateData(NameInfo info) {
        DataStoreKey key = getKey(info);
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
