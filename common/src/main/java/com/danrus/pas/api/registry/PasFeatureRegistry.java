package com.danrus.pas.api.registry;

import com.danrus.pas.api.feature.PasFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PasFeatureRegistry {
    private Map<Class<? extends PasFeature>, Function<String, ? extends PasFeature>> featureFactories;

    public <T extends PasFeature> void registerFeature(Class<T> name, Function<String, T> factory) {
        featureFactories.put(name, factory);
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

}
