package com.danrus.pas.core.pipeline;

import com.danrus.pas.api.request.PasRequest;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PasPipelineContext {
    private final PasRequest request;
    private final Map<String, Object> metadata;

    public PasPipelineContext(PasRequest request, Map<String, Object> metadata) {
        this.request = request;
        this.metadata = metadata;
    }

    public PasPipelineContext(PasRequest request) {
        this(request, new HashMap<>());
    }

    public PasRequest getRequest() {
        return request;
    }

    /**
     * Retrieve metadata from the context by key and type.
     *
     * @param key metadata key
     * @param type metadata type
     * @return metadata of context
     * @param <T> Any type of metadata
     */

    @Nullable public <T> T getMetadata(String key, Class<T> type) {
        Object data = metadata.get(key);
        if (type.isInstance(data)) {
            return type.cast(data);
        }
        return null;
    }

    public void setMetadata(String key, Object value) {
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
    }
}
