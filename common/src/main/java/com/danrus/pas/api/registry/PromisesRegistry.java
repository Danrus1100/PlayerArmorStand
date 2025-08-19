package com.danrus.pas.api.registry;

import com.danrus.pas.api.request.result.Promise;

import java.util.HashMap;
import java.util.Map;

public class PromisesRegistry<T extends Promise<?>>  {
    private Map<String, T> promises = new HashMap<>();

    public void register(String key, T promise) {
        promises.put(key, promise);
    }

    public T get(String key) {
        return promises.get(key);
    }

    public void unregister(String key) {
        promises.remove(key);
    }
}
