package com.danrus.pas.impl.features;

import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.managers.PasManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record SkinProviderFeature(String provider) implements RenameFeature {

    private static final String DEFAULT_PROVIDER = "M";

    @Override
    public @Nullable RenameFeature parseFrom(@NotNull String input) {
        List<String> providers = PasManager.getInstance().getExistingProviders();
        for (String prov : providers) {
            if (input.contains(prov)) {
                return new SkinProviderFeature(prov);
            }
        }
        return null;
    }

    @Override
    public @NotNull String compile() {
        return provider != null && !provider.equals(DEFAULT_PROVIDER) ? provider : "";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    // Explicit getter (in addition to record accessor)
    public String getProvider() { return provider; }
}
