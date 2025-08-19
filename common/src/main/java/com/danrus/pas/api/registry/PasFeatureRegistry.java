package com.danrus.pas.api.registry;

import com.danrus.pas.api.PasFeature;

import java.util.ArrayList;
import java.util.List;

public class PasFeatureRegistry {
    private final List<PasFeature> features = new ArrayList<>();

    public void addFeature (PasFeature feature) {
        this.features.add(feature);
    }

    public List<PasFeature> getFeatures() {
        return features;
    }
}
