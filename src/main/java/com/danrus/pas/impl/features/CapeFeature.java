package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapeFeature implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("C(?::([^%|]+)(?:%([^%|]+)%)?)?");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("C(?::[^%|]+(?:%[^%|]+%)?)?");

    private boolean enabled = false;
    private String provider = "M";
    private String id = "";

    @Override
    public boolean parse(@NotNull String input) {
        Matcher matcher = PARSE_PATTERN.matcher(input);

        if (matcher.find()) {
            this.enabled = true;

            String prov = matcher.group(1);
            String capeId = matcher.group(2);

            if (prov != null || capeId != null) {
                this.provider = prov == null ? "M" : prov.trim();
                this.id = capeId == null ? "" : capeId.trim();
            } else {
                this.provider = "M";
                this.id = "";
            }
            return true;
        }
        return false;
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

    @Override
    public void reset() {
        this.enabled = false;
        this.provider = "M";
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