package com.danrus.pas.api.request;

import com.danrus.pas.api.feature.PasFeature;

public record RequestData(String string, String sourceHint, PasFeature[] features) {
    public RequestData(String string) {
        this(string, PasFeature.resolveSourceHint(PasFeature.of(string)), PasFeature.of(string));
    }
}
