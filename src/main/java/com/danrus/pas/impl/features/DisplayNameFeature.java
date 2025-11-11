package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class DisplayNameFeature implements RenameFeature {

    private static final String FLAG = "D:";
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("D:[^|]+");

    boolean enabled = false;
    String name = "";

    @Override
    public boolean parse(@NotNull String input) {
        if (input.contains(FLAG)) {
            this.enabled = true;
            this.name = input.substring(input.indexOf(FLAG) + 2);
            return true;
        }
        return false;
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

    @Override
    public void reset() {
        this.enabled = false;
        this.name = "";
    }

    public boolean isEnabled() { return enabled; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
