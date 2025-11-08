package com.danrus.pas.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public interface RenameFeature {
    boolean parse(@NotNull String input);
    @NotNull String compile();
    default int getPriority() { return 100; } // Bigger number = later parsing
    @Nullable default Pattern getCleanupPattern() { return null; }
    void reset();


    default boolean affectsIdentity() { return false; }
    default int identityHashCode() { return 0; }
    default boolean identityEquals(RenameFeature other) {
        return other != null && this.getClass().equals(other.getClass());
    }
}
