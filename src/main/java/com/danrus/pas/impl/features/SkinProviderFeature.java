package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.Rl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkinProviderFeature implements RenameFeature {

    public static final ResourceLocation NAMEMC_LOGO = Rl.pas("namemc");
    public static final ResourceLocation FILE_LOGO = Rl.pas("file");

    private static final String DEFAULT_PROVIDER = "M";

    private String provider = DEFAULT_PROVIDER;

    @Override
    public boolean parse(@NotNull String input) {
        List<String> providers = PasManager.getInstance().getExistingProviders();
        for (String prov : providers) {
            if (input.contains(prov)) {
                this.provider = prov;
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull String compile() {
        return provider != null && !provider.equals(DEFAULT_PROVIDER) ? provider : "";
    }

    @Override
    public @Nullable Component getDisplayText() {
        if (DEFAULT_PROVIDER.equals(provider)) return null;
        return Component.translatable("pas.menu.tab.skin");
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        switch (provider) {
            case "N" ->  { return NAMEMC_LOGO; }
            case "F" ->  { return FILE_LOGO; }
            default ->  { return null; }
        }
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void reset() {
        this.provider = DEFAULT_PROVIDER;
    }

    public String getProvider() { return provider; }
    public void setProvider(@Nullable String provider) {
        this.provider = provider == null ? DEFAULT_PROVIDER : provider;
    }
}
