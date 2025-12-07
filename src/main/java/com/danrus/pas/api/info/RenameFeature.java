package com.danrus.pas.api.info;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public interface RenameFeature {
    boolean parse(@NotNull String input);
    @NotNull String compile();
    default int getPriority() { return 100; } // Bigger number = later parsing
    @Nullable default Pattern getCleanupPattern() { return null; }
    @Nullable default Component getDisplayText() { return null; }
    @Nullable default ResourceLocation getIcon() { return null; }
    default int getTextOffset() { return 18; }
    void reset();
}
