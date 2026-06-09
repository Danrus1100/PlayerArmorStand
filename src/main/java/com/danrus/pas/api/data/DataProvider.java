package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;

import java.util.Map;
import java.util.Optional;

public interface DataProvider<T extends DataHolder> {
    Optional<T> get(NameInfo info);
    Optional<T> find(NameInfo info);
    boolean delete(NameInfo info);
    Map<NameInfo, T> getAll();
    void store(NameInfo info, T data);
    void invalidateData(NameInfo info);
    String getName();
    DataType getDataType();
    default void clearCache() {}
}
