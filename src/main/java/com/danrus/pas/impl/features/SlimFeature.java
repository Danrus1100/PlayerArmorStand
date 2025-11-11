package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import org.jetbrains.annotations.NotNull;

public class SlimFeature implements RenameFeature {

    private static final String FLAG = "S";
    private boolean slim = false;

    @Override
    public boolean parse(@NotNull String input) {
        if (input.contains(FLAG)) {
            this.slim = true;
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String compile() {
        return slim ? FLAG : "";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void reset() {
        this.slim = false;
    }

    public boolean isSlim() { return slim; }
    public void setSlim(boolean slim) { this.slim = slim; }
}
