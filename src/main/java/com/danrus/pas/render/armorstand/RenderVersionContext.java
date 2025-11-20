package com.danrus.pas.render.armorstand;

import java.util.HashMap;
import java.util.Map;

public class RenderVersionContext {
    private final Map<Class<?>, Object> contextMap = new HashMap<>(16);
    private final Cape cape;

    public RenderVersionContext(Cape cape) {
        this.cape = cape;
    }

    public <T> void putData(T data) {
        if (contextMap.size() >= 16) {
            throw new IllegalStateException("RenderVersionContext can hold up to 16 data entries.");
        }
        if (data != null) {
            contextMap.put(data.getClass(), data);
        }
    }

    public <T> T getData(Class<T> clazz) {
        try {
            if (!contextMap.containsKey(clazz)) {
                throw new IllegalArgumentException("No data found for class: " + clazz.getName());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving data for class: " + clazz.getName(), e);
        }
        return clazz.cast(contextMap.get(clazz));
    }

    public Cape getCape() {
        return cape;
    }
}
