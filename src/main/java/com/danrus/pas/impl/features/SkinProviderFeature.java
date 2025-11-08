package com.danrus.pas.impl.features;

import com.danrus.pas.api.RenameFeature;
import com.danrus.pas.managers.PasManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SkinProviderFeature implements RenameFeature {

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
    public int getPriority() {
        return 2;
    }

    @Override
    public void reset() {
        this.provider = DEFAULT_PROVIDER;
    }

    @Override
    public boolean affectsIdentity() {
        return true;
    }

    @Override
    public int identityHashCode() {
        return provider.hashCode();
    }

    @Override
    public boolean identityEquals(RenameFeature other) {
        if (!(other instanceof SkinProviderFeature)) {
            return false;
        }
        return Objects.equals(this.provider, ((SkinProviderFeature) other).provider);
    }

    public String getProvider() { return provider; }
    public void setProvider(@Nullable String provider) {
        this.provider = provider == null ? DEFAULT_PROVIDER : provider;
    }
}
