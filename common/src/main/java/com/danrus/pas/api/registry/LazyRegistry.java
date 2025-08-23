package com.danrus.pas.api.registry;

import com.danrus.pas.api.request.result.LazyResult;

import java.util.HashMap;
import java.util.Map;

public class LazyRegistry<T extends LazyResult<?>>  {
    private Map<String, T> lazies = new HashMap<>();

    public void register(String key, T promise) {
        lazies.put(key, promise);
    }

    public T get(String key) {
        return lazies.get(key);
    }

    public void unregister(String key) {
        lazies.remove(key);
    }
}
