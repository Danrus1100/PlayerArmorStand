package com.danrus.pas.api.registry;

import com.danrus.pas.api.request.result.LazyResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LazyRegistry {
    private final Map<String, LazyResult<?>> lazies = new HashMap<>();

    public void register(String key, LazyResult<?> lazy) {
        lazies.put(key, lazy);
    }

    @SuppressWarnings("unchecked")
    public <T> LazyResult<T> get(String key, Class<T> type) {
        LazyResult<?> lazy = lazies.get(key);
        if (lazy != null && type.isInstance(lazy.get())) {
            return (LazyResult<T>) lazy;
        }
        return null;
    }

    public <T> LazyResult<T> get(UUID key, Class<T> type) {
        return get(key.toString(), type);
    }

    public void unregister(String key) {
        lazies.remove(key);
    }
}
