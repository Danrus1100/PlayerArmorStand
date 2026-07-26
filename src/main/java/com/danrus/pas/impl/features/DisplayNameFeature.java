package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public record DisplayNameFeature(boolean enabled, String name) implements RenameFeature {

    private static final String FLAG = "D:";
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("D:[^|]+");

    @Override
    public @Nullable RenameFeature parseFrom(@NotNull String input) {
        if (input.contains(FLAG)) {
            return new DisplayNameFeature(true, input.substring(input.indexOf(FLAG) + 2));
        }
        return null;
    }

    @Override
    public @Nullable RenameFeature parseToken(@NotNull String token) {
        return token.startsWith(FLAG)
                ? new DisplayNameFeature(true, token.substring(FLAG.length()))
                : null;
    }

    @Override
    public @NotNull String compile() {
        if (!enabled) return "";
        return FLAG + name;
    }

    @Override
    public @Nullable Pattern getCleanupPattern() {
        return CLEANUP_PATTERN;
    }

    // Explicit getters (in addition to record accessors)
    public boolean isEnabled() { return enabled; }
    public String getName() { return name; }
}
