package com.danrus.pas.render.armorstand;

import com.danrus.pas.render.PasRenderContext;

import java.util.HashMap;
import java.util.Map;

public class RenderVersionContext implements PasRenderContext {
    private final Map<String, Object> contextMap = new HashMap<>(16);
    private final Cape cape;

    public RenderVersionContext(Cape cape) {
        this.cape = cape;
    }

    public <T> PasRenderContext putData(T data, String type) {
        if (contextMap.size() >= 16) {
            throw new IllegalStateException("RenderVersionContext can hold up to 16 data entries.");
        }
        if (data != null) {
            contextMap.put(type, data);
        }
        return this;
    }

    public <T> T getData(Class<T> clazz, String type) {
        try {
            if (!contextMap.containsKey(type)) {
                throw new IllegalArgumentException("No data found for class: " + clazz.getName());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving data for class: " + clazz.getName(), e);
        }
        return clazz.cast(contextMap.get(type));
    }

    public Cape getCape() {
        return cape;
    }
}
