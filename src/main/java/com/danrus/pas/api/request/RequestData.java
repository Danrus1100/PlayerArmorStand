package com.danrus.pas.api.request;

import com.danrus.pas.api.PasFeature;

public record RequestData(String baseName, String sourceHint, PasFeature[] features) {
    public RequestData(String baseName, String sourceHint) {
        this(baseName, sourceHint, new PasFeature[0]);
    }

    public RequestData(String baseName) {
        this(baseName, "", new PasFeature[0]);
    }

}
