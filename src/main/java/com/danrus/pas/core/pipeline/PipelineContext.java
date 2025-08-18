package com.danrus.pas.core.pipeline;

import com.danrus.pas.api.request.PasRequest;

import java.util.HashMap;
import java.util.Map;

public class PipelineContext {
    private final PasRequest request;
    private final Map<String, Object> metadata;

    public PipelineContext(PasRequest request, Map<String, Object> metadata) {
        this.request = request;
        this.metadata = metadata;
    }

    public PipelineContext(PasRequest request) {
        this(request, new HashMap<>());
    }

    public PasRequest getRequest() {
        return request;
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public void setMetadata(String key, Object value) {
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
    }
}
