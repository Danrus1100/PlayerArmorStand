package com.danrus.pas.render.common;
import java.util.HashMap;
import java.util.Map;

public class PasRenderContext {

    public PasRenderContext() {}

    private final Map<String, Object> contextMap = new HashMap<>(16);

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

    //? >= 1.21.9 {
    public static PasRenderContext create(net.minecraft.client.renderer.SubmitNodeCollector collector) {
        return new PasRenderContext().putData(collector, "collector");
    }
    //?}
}
