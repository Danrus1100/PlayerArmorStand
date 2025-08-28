package com.danrus.pas.api.registry;

import com.danrus.pas.api.feature.IPasFeature;

import java.util.ArrayList;
import java.util.List;

public class PasFeatureRegistry {
    private final List<IPasFeature> features = new ArrayList<>();

    public void addFeature (IPasFeature feature) {
        this.features.add(feature);
    }

    public List<IPasFeature> getFeatures() {
        return features;
    }
}
