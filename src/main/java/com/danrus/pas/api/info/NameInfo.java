package com.danrus.pas.api.info;

import com.danrus.pas.api.reg.FeatureRegistry;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.OverlayFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.features.SlimFeature;
import com.danrus.pas.impl.features.DisplayNameFeature;
import com.danrus.pas.utils.NIParser;
import com.danrus.pas.utils.Rl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public record NameInfo(
        String base,
        Map<Class<? extends RenameFeature>, RenameFeature> features,
        String legacyParams,
        @Nullable ResourceLocation lolmeme
) {
    public static Map<String, NameInfo> MEMES = Map.of(
            "данечка разработчик", meme(Rl.pas("textures/lol/danechka_razrabotchik.png")),
            "дакимакура", meme(Rl.pas("textures/lol/dakimakura.png")),
            "гига крео", meme(Rl.pas("textures/lol/gigakreo.png")),
            "strange link", meme(Rl.pas("textures/lol/link.png")),
            "странная ссылка", meme(Rl.pas("textures/lol/link.png")),
            "сисюлики", meme(Rl.pas("textures/lol/boobs.png")),
            "джастик", meme(Rl.pas("textures/lol/justik.png"))
    );

    private static NameInfo meme(ResourceLocation texture) {
        return new NameInfo("", defaultFeatures(), "", texture);
    }

    public NameInfo {
        base = base == null ? "" : base;
        features = features == null ? Map.of() : Map.copyOf(features);
        legacyParams = legacyParams == null ? "" : legacyParams;
    }

    public NameInfo() {
        this("", defaultFeatures(), "", null);
    }

    public NameInfo(String base) {
        this(base, defaultFeatures(), "", null);
    }

    static Map<Class<? extends RenameFeature>, RenameFeature> defaultFeatures() {
        Map<Class<? extends RenameFeature>, RenameFeature> m = new LinkedHashMap<>();
        for (RenameFeature f : FeatureRegistry.getInstance().getOrderedDefaults()) {
            m.put(f.getClass(), f);
        }
        return m;
    }

    // --- Parse ---

    public static NameInfo parse(Component input) {
        if (input != null) {
            return parse(input.getString());
        }
        return new NameInfo();
    }

    public static NameInfo parse(String input) {
        String lower = input.toLowerCase();
        if (PasConfig.getInstance().isShowEasterEggs()) {
            NameInfo meme = MEMES.get(lower);
            if (meme != null) {
                return meme;
            }
        }
        return NIParser.getInstance().parse(input);
    }

    // --- Compile ---

    public String compile() {
        StringBuilder out = new StringBuilder();
        out.append(base == null ? "" : base);

        // Iterate features in registry order for deterministic output
        List<String> featureParts = FeatureRegistry.getInstance().getOrderedDefaults().stream()
                .map(def -> {
                    RenameFeature f = features.get(def.getClass());
                    return f != null ? f.compile() : "";
                })
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

    // --- API ---

    public boolean isEmpty() { return base == null || base.isEmpty(); }

    @SuppressWarnings("unchecked")
    public <T extends RenameFeature> T getFeature(Class<T> featureClass) {
        return (T) features.get(featureClass);
    }

    public NameInfo.Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public @NotNull String toString() {
        return "NameInfo[" + this.compile() + "(" + this.hashCode() + ")]";
    }

    // Custom equals/hashCode: base + features only (exclude legacyParams and lolmeme)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NameInfo other)) return false;
        return Objects.equals(this.base, other.base) && this.features.equals(other.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, features);
    }

    // --- Read-only convenience getters (used by translators/rendering) ---

    public String getDesiredProvider() {
        SkinProviderFeature feature = getFeature(SkinProviderFeature.class);
        return feature != null ? feature.getProvider() : "M";
    }

    public boolean wantBeSlim() {
        SlimFeature feature = getFeature(SlimFeature.class);
        return feature != null && feature.isSlim();
    }

    public boolean wantCape() {
        CapeFeature feature = getFeature(CapeFeature.class);
        return feature != null && feature.isEnabled();
    }

    public int blend() {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        return feature != null ? feature.getBlend() : 100;
    }

    public String overlay() {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        return feature != null ? feature.getTexture() : "";
    }

    public String capeProvider() {
        CapeFeature feature = getFeature(CapeFeature.class);
        return feature != null ? feature.getProvider() : "";
    }

    // --- Builder (mutable editing façade for the GUI) ---

    public static class Builder {
        private String base;
        private final LinkedHashMap<Class<? extends RenameFeature>, RenameFeature> features;
        private String legacyParams;
        private ResourceLocation lolmeme;

        public Builder(NameInfo src) {
            this.base = src.base();
            this.features = new LinkedHashMap<>(src.features());
            this.legacyParams = src.legacyParams();
            this.lolmeme = src.lolmeme();
        }

        // Read methods mirroring NameInfo
        @SuppressWarnings("unchecked")
        public <T extends RenameFeature> T getFeature(Class<T> featureClass) {
            return (T) features.get(featureClass);
        }

        public String getDesiredProvider() {
            SkinProviderFeature f = getFeature(SkinProviderFeature.class);
            return f != null ? f.getProvider() : "M";
        }

        public boolean wantBeSlim() {
            SlimFeature f = getFeature(SlimFeature.class);
            return f != null && f.isSlim();
        }

        public boolean wantCape() {
            CapeFeature f = getFeature(CapeFeature.class);
            return f != null && f.isEnabled();
        }

        public int blend() {
            OverlayFeature f = getFeature(OverlayFeature.class);
            return f != null ? f.getBlend() : 100;
        }

        public String overlay() {
            OverlayFeature f = getFeature(OverlayFeature.class);
            return f != null ? f.getTexture() : "";
        }

        public String capeProvider() {
            CapeFeature f = getFeature(CapeFeature.class);
            return f != null ? f.getProvider() : "";
        }

        public String compile() {
            return build().compile();
        }

        public String base() { return base; }

        // Setters (fluent, each replaces the relevant immutable feature record)
        public Builder setBase(String base) {
            this.base = base == null ? "" : base;
            return this;
        }

        public Builder setSkinProvider(String p) {
            features.put(SkinProviderFeature.class, new SkinProviderFeature(p == null ? "M" : p));
            return this;
        }

        public Builder setSlim(boolean s) {
            features.put(SlimFeature.class, new SlimFeature(s));
            return this;
        }

        public Builder setCapeEnabled(boolean e) {
            CapeFeature cur = getFeature(CapeFeature.class);
            String prov = cur != null ? cur.getProvider() : "M";
            String id = cur != null ? cur.getId() : "";
            features.put(CapeFeature.class, new CapeFeature(e, prov, id));
            return this;
        }

        public Builder setCapeProvider(String p) {
            CapeFeature cur = getFeature(CapeFeature.class);
            boolean en = cur != null && cur.isEnabled();
            String id = cur != null ? cur.getId() : "";
            features.put(CapeFeature.class, new CapeFeature(en, p == null ? "M" : p, id));
            return this;
        }

        public Builder setCapeId(String id) {
            CapeFeature cur = getFeature(CapeFeature.class);
            boolean en = cur != null && cur.isEnabled();
            String prov = cur != null ? cur.getProvider() : "M";
            features.put(CapeFeature.class, new CapeFeature(en, prov, id == null ? "" : id));
            return this;
        }

        public Builder setOverlay(String tex) {
            OverlayFeature cur = getFeature(OverlayFeature.class);
            int b = cur != null ? cur.getBlend() : 100;
            features.put(OverlayFeature.class, new OverlayFeature(tex == null ? "" : tex, b));
            return this;
        }

        public Builder setBlend(int b) {
            OverlayFeature cur = getFeature(OverlayFeature.class);
            String tex = cur != null ? cur.getTexture() : "";
            features.put(OverlayFeature.class, new OverlayFeature(tex, b));
            return this;
        }

        public Builder setDisplayName(String n) {
            String name = n == null ? "" : n;
            features.put(DisplayNameFeature.class, new DisplayNameFeature(!name.isEmpty(), name));
            return this;
        }

        public NameInfo build() {
            return new NameInfo(base, new LinkedHashMap<>(features), legacyParams, lolmeme);
        }
    }
}
