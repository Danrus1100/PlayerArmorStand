package com.danrus.pas.api;

import java.util.HashMap;

public interface DataCache<T> {
    SkinData get(String string);
    boolean delete(String string);
    default HashMap<String, SkinData> getAll() {
        return new HashMap<String, SkinData>();
    }
    default void store(String name, Object data){}
    default void invalidateData(String name){}
    boolean isCompatibleWith(Object data);
    String getName();
}