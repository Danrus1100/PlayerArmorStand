package com.danrus.pas.api.info;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.reg.FeatureRegistry;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.OverlayFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.features.SlimFeature;
import com.danrus.pas.impl.features.DisplayNameFeature;
import com.danrus.pas.utils.info.NIParser;
import com.danrus.pas.utils.mc.Id;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public record NameInfo(
        String base,
        Map<Class<? extends RenameFeature>, RenameFeature> features,
        @Nullable Identifier lolmeme,
        int version
) implements NameInfoLike {
    public static final NameInfo EMPTY = new NameInfo();
    private static final Map<NameInfo, String> COMPILED_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());
    public static Map<String, NameInfo> MEMES = Map.of(
            "данечка разработчик", meme(Id.pas("textures/lol/danechka_razrabotchik.png")),
            "дакимакура", meme(Id.pas("textures/lol/dakimakura.png")),
            "гига крео", meme(Id.pas("textures/lol/gigakreo.png")),
            "strange link", meme(Id.pas("textures/lol/link.png")),
            "странная ссылка", meme(Id.pas("textures/lol/link.png")),
            "сисюлики", meme(Id.pas("textures/lol/boobs.png"))
    );

    private static NameInfo meme(Identifier texture) {
        return new NameInfo("", defaultFeatures(), texture, 2);
    }

    public NameInfo {
        base = base == null ? "" : base;
        features = features == null ? Map.of() : Map.copyOf(features);
    }

    public NameInfo() {
        this("", defaultFeatures(), null, 2);
    }

    public NameInfo(String base) {
        this(base, defaultFeatures(), null, 2);
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
        return NameInfo.EMPTY;
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

    public String compileFast() {
        return COMPILED_CACHE.computeIfAbsent(this, NameInfo::compile);
    }

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

        if (!featureParts.isEmpty()) {
            if (version == 1) {
                out.append("|").append(String.join("", featureParts));
            } else {
                out.append("||").append(String.join(";", featureParts));
            }
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
    public @NotNull NameInfoPattern toPattern() {
        return NameInfoPattern.exact(this);
    }

    public NameInfoPattern toBaseNamePattern() {
        return NameInfoPattern.any().withBaseName(base);
    }

    @Override
    public @NotNull String toString() {
        return this.compile();
    }

    // Custom equals/hashCode: texture identity plus the name format version.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NameInfo other)) return false;
        if (lolmeme != null || other.lolmeme != null) {
            return version == other.version && Objects.equals(lolmeme, other.lolmeme);
        }
        return version == other.version
                && Objects.equals(base, other.base)
                && features.equals(other.features);
    }

    @Override
    public int hashCode() {
        if (lolmeme != null) return Objects.hash(lolmeme, version);
        return Objects.hash(base, features, version);
    }

    // --- Read-only convenience getters (used by translators/rendering) ---

    public String getDesiredProvider() {
        SkinProviderFeature feature = getFeature(SkinProviderFeature.class);
        return feature != null ? feature.getProvider() : "M";
    }

    public boolean isSlim() {
        SlimFeature feature = getFeature(SlimFeature.class);
        return feature != null && feature.isSlim();
    }

    public boolean hasCape() {
        CapeFeature feature = getFeature(CapeFeature.class);
        return feature != null && feature.isEnabled();
    }

    public int overlayBlend() {
        OverlayFeature feature = getFeature(OverlayFeature.class);
        return feature != null ? feature.getBlend() : 100;
    }

    public String overlayTexture() {
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
        private Identifier lolmeme;
        private int version = 2;

        public Builder(NameInfo src) {
            this.base = src.base();
            this.features = new LinkedHashMap<>(src.features());
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

        public boolean isSlim() {
            SlimFeature f = getFeature(SlimFeature.class);
            return f != null && f.isSlim();
        }

        public boolean hasCape() {
            CapeFeature f = getFeature(CapeFeature.class);
            return f != null && f.isEnabled();
        }

        public int overlayBlend() {
            OverlayFeature f = getFeature(OverlayFeature.class);
            return f != null ? f.getBlend() : 100;
        }

        public String overlayTexture() {
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

        public Builder addFeature(RenameFeature feature) {
            features.put(feature.getClass(), feature);
            return this;
        }

        public Builder setSkinProvider(String p) {
            return addFeature(new SkinProviderFeature(p == null ? "M" : p));
        }

        public Builder setSlim(boolean s) {
            return addFeature(new SlimFeature(s));
        }

        public Builder setCapeEnabled(boolean e) {
            CapeFeature cur = getFeature(CapeFeature.class);
            String prov = cur != null ? cur.getProvider() : "M";
            String id = cur != null ? cur.getId() : "";
            return addFeature(new CapeFeature(e, prov, id));
        }

        public Builder setCapeProvider(String p) {
            CapeFeature cur = getFeature(CapeFeature.class);
            boolean en = cur != null && cur.isEnabled();
            String id = cur != null ? cur.getId() : "";
            return addFeature(new CapeFeature(en, p == null ? "M" : p, id));
        }

        public Builder setCapeId(String id) {
            CapeFeature cur = getFeature(CapeFeature.class);
            boolean en = cur != null && cur.isEnabled();
            String prov = cur != null ? cur.getProvider() : "M";
            return addFeature(new CapeFeature(en, prov, id == null ? "" : id));
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

        public Builder setVersion(int version) {
            if (version <= 0) throw new IllegalArgumentException("NameInfo version must be greater than 0");
            this.version = version;
            return this;
        }

        public NameInfo build() {
            return new NameInfo(base, new LinkedHashMap<>(features), lolmeme, version);
        }
    }
}
