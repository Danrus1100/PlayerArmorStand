package com.danrus.pas.api.reg;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FeatureRegistry {
    private static final FeatureRegistry INSTANCE = new FeatureRegistry();
    private final Map<Class<? extends RenameFeature>, RenameFeature> prototypes = new ConcurrentHashMap<>();
    private final List<Class<? extends RenameFeature>> orderedFeatures = new ArrayList<>();

    private FeatureRegistry() {}

    public static FeatureRegistry getInstance() { return INSTANCE; }

    public <T extends RenameFeature> void register(@NotNull Class<T> featureClass) {
        try {
            T instance = featureClass.getDeclaredConstructor().newInstance();
            prototypes.put(featureClass, instance);
            orderedFeatures.add(featureClass);
            orderedFeatures.sort(Comparator.comparingInt(cls -> {
                try {
                    return cls.getDeclaredConstructor().newInstance().getPriority();
                } catch (Exception e) {
                    return 100;
                }
            }));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Feature class must have a public no-arg constructor: " + featureClass.getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register feature: " + featureClass.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends RenameFeature> T createFeature(@NotNull Class<T> featureClass) {
        try {
            return featureClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Class<? extends RenameFeature>> getOrderedFeatures() {
        return Collections.unmodifiableList(orderedFeatures);
    }
}
