package com.danrus.pas.api;

import java.util.HashMap;

public interface DataProvider<T extends DataHolder> {
    T get(NameInfo info);
    boolean delete(NameInfo info);
    HashMap <String, T> getAll();
    void store(NameInfo info, T data);
    void invalidateData(NameInfo info);
    String getName();
}