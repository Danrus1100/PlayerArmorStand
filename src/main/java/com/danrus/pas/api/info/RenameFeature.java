package com.danrus.pas.api.info;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public interface RenameFeature {
    @Nullable RenameFeature parseFrom(@NotNull String input); // NEW instance if present, else null
    @NotNull String compile();
    default int getPriority() { return 100; }
    @Nullable default Pattern getCleanupPattern() { return null; }
}
