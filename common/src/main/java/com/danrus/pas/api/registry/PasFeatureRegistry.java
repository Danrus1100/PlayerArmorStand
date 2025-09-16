package com.danrus.pas.api.registry;

import com.danrus.pas.api.feature.PasFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PasFeatureRegistry {
    private final Map<Class<? extends PasFeature>, Function<String, ? extends PasFeature>> featureFactories;
    private final Map<Class<? extends PasFeature>, Function<String, String>> locationsGetters;

    public PasFeatureRegistry() {
        this.featureFactories = new HashMap<>();
        this.locationsGetters = new HashMap<>();
    }

    public <T extends PasFeature> void registerFeature(Class<T> name, Function<String, T> factory) {
        featureFactories.put(name, factory);
    }

    public <T extends PasFeature> void registerFeature(Class<T> name, Function<String, T> factory, Function<String, String> locationGetter) {
        featureFactories.put(name, factory);
        registerLocationGetter(name, locationGetter);
    }

    public <T extends PasFeature> void registerLocationGetter(Class<T> name, Function<String, String> locationGetter) {
        locationsGetters.put(name, locationGetter);
    }

    public PasFeature createFeature(Class<? extends PasFeature> clazz, String string) {
        Function<String, ? extends PasFeature> factory = featureFactories.get(clazz);
        if (factory == null) {
            throw new IllegalArgumentException("Feature not registered: " + clazz.getSimpleName());
        }
        return factory.apply(string);
    }

    public PasFeature[] createFeatures(String string){
        List<PasFeature> features = new ArrayList<>();
        for (Class<? extends PasFeature> k : featureFactories.keySet()) {
            features.add(createFeature(k, string));
        }
        return features.toArray(new PasFeature[0]);
    }

    public Map<Class<? extends PasFeature>, Function<String, String>> getLocationsGetters() {
        return this.locationsGetters;
    }

}
