package com.danrus.pas.api;

import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.OverlayFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.features.SlimFeature;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NameInfo {

    private final Map<Class<? extends RenameFeature>, RenameFeature> features = new LinkedHashMap<>();
    private String base;
    private String legacyParams;

    public NameInfo() { this(""); }
    public NameInfo(String base) {
        this.base = base == null ? "" : base;
        initializeFeatures();
    }

    private void initializeFeatures() {
        for (Class<? extends RenameFeature> featureClass : FeatureRegistry.getInstance().getOrderedFeatures()) {
            RenameFeature feature = FeatureRegistry.getInstance().createFeature(featureClass);
            if (feature != null) {
                features.put(featureClass, feature);
            }
        }
    }

    public static NameInfo parse(Component input) {
        if (input != null) {
            return parse(input.getString());
        }
        return new NameInfo();
    }

    public static NameInfo parse(String input) {
        if (input == null || input.isEmpty()) return new NameInfo();

        String[] divided = input.split("\\|", 2);
        String name = divided[0].trim();

        if (name.matches(".*[<>:\"/\\\\?*].*")) return new NameInfo();

        NameInfo info = new NameInfo(name);

        if (divided.length > 1) {
            String params = divided[1].trim();

            for (Class<? extends RenameFeature> featureClass : FeatureRegistry.getInstance().getOrderedFeatures()) {
                RenameFeature feature = info.getFeature(featureClass);
                if (feature != null && feature.parse(params)) {
                    String compiled = feature.compile();
                    params = params.replace(compiled, "").trim();
                }
            }

            info.legacyParams = normalizeParams(params);
        }

        return info;
    }

    public String compile() {
        StringBuilder out = new StringBuilder();
        out.append(base == null ? "" : base);

        List<String> featureParts = features.values().stream()
                .map(RenameFeature::compile)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (legacyParams != null && !legacyParams.isEmpty()) {
            featureParts.add(0, legacyParams);
        }

        if (!featureParts.isEmpty()) {
            out.append("|").append(String.join("", featureParts));
        }

        return out.toString();
    }

    private static String normalizeParams(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String p = raw.replaceAll("\\s+", "").toUpperCase();

        // Убираем все известные паттерны фич динамически
        for (Class<? extends RenameFeature> featureClass : FeatureRegistry.getInstance().getOrderedFeatures()) {
            RenameFeature feature = FeatureRegistry.getInstance().createFeature(featureClass);
            if (feature != null) {
                Pattern pattern = feature.getCleanupPattern();
                if (pattern != null) {
                    p = pattern.matcher(p).replaceAll("");
                }
            }
        }

        // Дедупликация символов
        StringBuilder sb = new StringBuilder();
        boolean[] seen = new boolean[256];
        for (int i = 0; i < p.length(); i++) {
            char ch = p.charAt(i);
            if (ch < 256 && !seen[ch]) {
                seen[ch] = true;
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    // --- API ---

    public boolean isEmpty() { return base == null || base.isEmpty(); }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends RenameFeature> T getFeature(Class<T> featureClass) {
        return (T) features.get(featureClass);
    }

    public void setName(String newName) { this.base = newName == null ? "" : newName; }
    public String base() { return base; }
    public String legacyParams() { return legacyParams; }

    @Override
    public @NotNull String toString() {
        return "NameInfo[base=" + base + ", features=" + features.size() + "]";
    }

    // --- Legacy ---

    @Deprecated
    public String getDesiredProvider() {
        SkinProviderFeature feature = getFeature(SkinProviderFeature.class);
        return feature != null ? feature.getProvider() : "M";
    }

    @Deprecated
    public boolean wantBeSlim() {
        SlimFeature feature = getFeature(SlimFeature.class);
        return feature != null && feature.isSlim();
    }

    @Deprecated
    public void setSlim(boolean slim) {
        SlimFeature feature = getFeature(SlimFeature.class);
        if (feature != null) {
            feature.setSlim(slim);
        }
    }

    @Deprecated
    public boolean wantCape() {
        CapeFeature feature = getFeature(CapeFeature.class);
        return feature != null && feature.isEnabled();
    }

    @Deprecated
    public void setCape(boolean cape) {
        CapeFeature feature = getFeature(CapeFeature.class);
        if (feature != null) {
            feature.setEnabled(cape);
        }
    }

    @Deprecated
    public void setOverlay(String texture) {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        if (feature != null) {
            feature.setTexture(texture);
        }
    }

    @Deprecated
    public void setBlend(int blend) {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        if (feature != null) {
            feature.setBlend(blend);
        }
    }

    @Deprecated
    public int blend() {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        return feature != null ? feature.getBlend() : 100;
    }

    @Deprecated
    public String overlay() {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        return feature != null ? feature.getTexture() : "";
    }

    @Deprecated
    public void setProvider(String provider) {
        SkinProviderFeature feature = getFeature(SkinProviderFeature.class);
        if (feature != null) {
            feature.setProvider(provider);
        }
    }

    @Deprecated
    public String capeProvider() {
        CapeFeature feature = getFeature(CapeFeature.class);
        return feature != null ? feature.getProvider() : "";
    }

}
