package com.danrus.pas.api;

import java.util.HashMap;

public interface DataHolder<T> {
    SkinData get(NameInfo info);
    boolean delete(NameInfo info);
    default HashMap<String, SkinData> getAll() {
        return new HashMap<String, SkinData>();
    }
    default void store(NameInfo info, Object data){}
    default void invalidateData(NameInfo info){}
    boolean isCompatibleWith(Object data);
    String getName();
}