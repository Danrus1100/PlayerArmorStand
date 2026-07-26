package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record OverlayFeature(String texture, int blend) implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("T:([^%|]+)%?(\\d*)");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("T:[^|]+");

    @Override
    public @Nullable RenameFeature parseFrom(@NotNull String input) {
        return parse(input, false);
    }

    @Override
    public @Nullable RenameFeature parseToken(@NotNull String token) {
        return parse(token, true);
    }

    private RenameFeature parse(String input, boolean entireInput) {
        Matcher matcher = PARSE_PATTERN.matcher(input);
        if (entireInput ? matcher.matches() : matcher.find()) {
            String tex = matcher.group(1).trim();
            String blendStr = matcher.group(2);
            int resolvedBlend = blendStr.isEmpty() ? 100 : clamp(Integer.parseInt(blendStr), 0, 100);
            return new OverlayFeature(tex, resolvedBlend);
        }
        return null;
    }

    @Override
    public @NotNull String compile() {
        if (texture == null || texture.isEmpty()) return "";
        return "T:" + texture + "%" + blend;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Nullable
    @Override
    public Pattern getCleanupPattern() {
        return CLEANUP_PATTERN;
    }

    // Explicit getters (in addition to record accessors)
    public String getTexture() { return texture; }
    public int getBlend() { return blend; }
    public boolean isEnabled() { return texture != null && !texture.isEmpty(); }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
