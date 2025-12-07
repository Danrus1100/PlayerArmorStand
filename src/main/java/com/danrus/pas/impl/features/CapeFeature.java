package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.utils.Rl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapeFeature implements RenameFeature {

    private static final Pattern PARSE_PATTERN = Pattern.compile("C(?::([^%|]+)(?:%([^%|]+)%)?)?");
    private static final Pattern CLEANUP_PATTERN = Pattern.compile("C(?::[^%|]+(?:%[^%|]+%)?)?");

    public static final ResourceLocation MOJANG_LOGO = Rl.pas("mojang");
    public static final ResourceLocation NAMEMC_LOGO = Rl.pas("namemc");
    public static final ResourceLocation MCCAPES_LOGO = Rl.pas("minecraftcapes");

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
    public @Nullable Component getDisplayText() {
        if (!enabled) return null;
        return Component.translatable("pas.menu.tab.cape");
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        if (!enabled) return null;

        switch (provider) {
            case "M":
                return MOJANG_LOGO;
            case "A":
                return NAMEMC_LOGO;
            case "I":
                return MCCAPES_LOGO;
            default:
                return null;
        }
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