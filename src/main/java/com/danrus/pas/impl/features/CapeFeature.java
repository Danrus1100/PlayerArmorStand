package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record CapeFeature(boolean enabled, String provider, String id) implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("C(?::([^%|]+)(?:%([^%|]+)%)?)?");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("C(?::[^%|]+(?:%[^%|]+%)?)?");

    @Override
    public @Nullable RenameFeature parseFrom(@NotNull String input) {
        Matcher matcher = PARSE_PATTERN.matcher(input);
        if (matcher.find()) {
            String prov = matcher.group(1);
            String capeId = matcher.group(2);
            String resolvedProv;
            String resolvedId;
            if (prov != null || capeId != null) {
                resolvedProv = prov == null ? "M" : prov.trim();
                resolvedId = capeId == null ? "" : capeId.trim();
            } else {
                resolvedProv = "M";
                resolvedId = "";
            }
            return new CapeFeature(true, resolvedProv, resolvedId);
        }
        return null;
    }

    @Override
    public @NotNull String compile() {
        if (!enabled) return "";
        StringBuilder sb = new StringBuilder("C");
        if (!provider.isEmpty() && !provider.equals("M")) {
            sb.append(":").append(provider);
        }
        if (!id.isEmpty() && !provider.equals("M")) {
            sb.append("%").append(id).append("%");
        }
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Nullable
    @Override
    public Pattern getCleanupPattern() {
        return CLEANUP_PATTERN;
    }

    // Explicit getters (in addition to record accessors)
    public boolean isEnabled() { return enabled; }
    public String getProvider() { return provider; }
    public String getId() { return id; }
}
