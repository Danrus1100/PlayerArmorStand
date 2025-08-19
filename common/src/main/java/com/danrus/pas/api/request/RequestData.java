package com.danrus.pas.api.request;

import com.danrus.pas.api.PasFeature;

public record RequestData(String baseName, String sourceHint, PasFeature[] features) {
    public RequestData(String baseName, String sourceHint) {
        this(baseName, sourceHint, new PasFeature[0]);
    }

    public RequestData(String baseName) {
        this(baseName, "", new PasFeature[0]);
    }


//    public static RequestData parse(String name) {
//        String[] parts = name.split("\\|", 2);
//        String baseName = parts[0].trim();
//
//    }
}
