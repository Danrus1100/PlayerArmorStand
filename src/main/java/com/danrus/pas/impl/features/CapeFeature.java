package com.danrus.pas.impl.features;

import com.danrus.pas.api.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapeFeature implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("C(?::([^%|]+)%([^|]+))?");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("C(?::[^|]+)?");

    private boolean enabled = false;
    private String provider = "";
    private String id = "";

    @Override
    public boolean parse(@NotNull String input) {
        Matcher matcher = PARSE_PATTERN.matcher(input);
        if (matcher.find()) {
            this.enabled = true;
            String prov = matcher.group(1);
            String capeId = matcher.group(2);
            this.provider = prov != null ? prov.trim() : "";
            this.id = capeId != null ? capeId.trim() : "";
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String compile() {
        if (!enabled) return "";

        StringBuilder sb = new StringBuilder("C");
        if (!provider.isEmpty() && !id.isEmpty()) {
            sb.append(":").append(provider).append("%").append(id);
        }
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public void reset() {
        this.enabled = false;
        this.provider = "";
        this.id = "";
    }

    @Nullable
    @Override
    public Pattern getCleanupPattern() {
        return CLEANUP_PATTERN;
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) {
        this.provider = provider == null ? "" : provider;
    }

    public String getId() { return id; }
    public void setId(String id) {
        this.id = id == null ? "" : id;
    }
}
