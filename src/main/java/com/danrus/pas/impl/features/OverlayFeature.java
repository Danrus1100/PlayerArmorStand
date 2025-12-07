package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OverlayFeature implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("T:([^%|]+)%?(\\d*)");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("T:[^|]+");

    private String texture = "";
    private int blend = 100;

    @Override
    public boolean parse(@NotNull String input) {
        Matcher matcher = PARSE_PATTERN.matcher(input);
        if (matcher.find()) {
            this.texture = matcher.group(1).trim();
            String blendStr = matcher.group(2);
            this.blend = blendStr.isEmpty() ? 100 : clamp(Integer.parseInt(blendStr), 0, 100);
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String compile() {
        if (texture == null || texture.isEmpty()) return "";
        return "T:" + texture + "%" + blend;
    }

    @Override
    public @Nullable Component getDisplayText() {
        if (texture.isEmpty()) return null;
        MutableComponent component = Component.translatable("pas.tooltip.overlay", texture);
        if (blend != 100) {
            component.append(Component.translatable("pas.tooltip.overlay.blended", String.valueOf(blend)));
        }
        return component;
    }

    @Override
    public int getTextOffset() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public void reset() {
        this.texture = "";
        this.blend = 100;
    }

    @Nullable
    @Override
    public Pattern getCleanupPattern() {
        return CLEANUP_PATTERN; // Удаляем из legacy params
    }

    public String getTexture() { return texture; }
    public void setTexture(String texture) {
        this.texture = texture == null ? "" : texture;
    }

    public int getBlend() { return blend; }
    public void setBlend(int blend) {
        this.blend = clamp(blend, 0, 100);
    }

    public boolean isEnabled() {
        return !texture.isEmpty();
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
