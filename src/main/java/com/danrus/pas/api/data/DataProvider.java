package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;

import java.util.HashMap;
import java.util.Optional;

public interface DataProvider<T extends DataHolder> {
    Optional<T> get(NameInfo info);
    Optional<T> find(NameInfo info);
    boolean delete(NameInfo info);
    boolean delete(DataStoreKey key);
    HashMap <DataStoreKey, T> getAll();
    void store(NameInfo info, T data);
    void store(DataStoreKey key, T data);
    void invalidateData(NameInfo info);
    String getName();
    DataStoreKey.DataType getDataType();
}