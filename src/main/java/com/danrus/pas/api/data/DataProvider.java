package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.utils.info.NameInfoMap;

import java.util.Collection;
import java.util.Optional;

public interface DataProvider<T extends DataHolder> {
    Optional<T> get(NameInfo info);
    Optional<T> findFirst(NameInfoLike infoLike);
    Collection<T> findAll(NameInfoLike infoLike);
    Optional<T> peek(NameInfo info);
    boolean deleteAllOf(NameInfoLike info);
    NameInfoMap<T> getAll();
    void store(NameInfo info, T data);
    void invalidateData(NameInfoLike info);
    String getName();
    DataType getDataType();
    default void clearCache() {}
}
