package com.danrus.pas.api.request;

import com.danrus.pas.api.feature.IPasFeature;

public record RequestData(String baseName, String sourceHint, IPasFeature[] features) {
    public RequestData(String baseName, String sourceHint) {
        this(baseName, sourceHint, new IPasFeature[0]);
    }

    public RequestData(String baseName) {
        this(baseName, "", new IPasFeature[0]);
    }


//    public static RequestData parse(String name) {
//        String[] parts = name.split("\\|", 2);
//        String baseName = parts[0].trim();
//
//    }
}
