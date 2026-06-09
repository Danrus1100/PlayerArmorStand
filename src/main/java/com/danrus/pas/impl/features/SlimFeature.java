package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SlimFeature(boolean slim) implements RenameFeature {

    private static final String FLAG = "S";

    @Override
    public @Nullable RenameFeature parseFrom(@NotNull String input) {
        return input.contains(FLAG) ? new SlimFeature(true) : null;
    }

    @Override
    public @NotNull String compile() {
        return slim ? FLAG : "";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    // Explicit getter (in addition to record accessor)
    public boolean isSlim() { return slim; }
}
