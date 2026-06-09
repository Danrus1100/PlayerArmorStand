package com.danrus.pas.api.reg;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FeatureRegistry {
    private static final FeatureRegistry INSTANCE = new FeatureRegistry();
    private final Map<Class<? extends RenameFeature>, RenameFeature> defaults = new LinkedHashMap<>();
    private volatile List<RenameFeature> ordered = List.of();

    private FeatureRegistry() {}

    public static FeatureRegistry getInstance() { return INSTANCE; }

    public synchronized void register(@NotNull RenameFeature def) {
        defaults.put(def.getClass(), def);
        List<RenameFeature> list = new ArrayList<>(defaults.values());
        list.sort(Comparator.comparingInt(RenameFeature::getPriority));
        ordered = List.copyOf(list);
    }

    public List<RenameFeature> getOrderedDefaults() { return ordered; }

    @SuppressWarnings("unchecked")
    public <T extends RenameFeature> T getDefault(Class<T> cls) {
        return (T) defaults.get(cls);
    }
}
