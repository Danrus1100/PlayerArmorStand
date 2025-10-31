package com.danrus.pas.impl.resolvers.common;

import com.danrus.pas.api.InfoResolver;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractSimpleResolver implements InfoResolver {
    @Override
    public boolean isApplicable(NameInfo info) {
        return getLiteral().equals(info.getFeature(SkinProviderFeature.class).getProvider());
    }

    @Override
    public ResourceLocation toResourceLocation(NameInfo info) {
        return Rl.pas(getPrefix() + "/" + info.base());
    }

    @Override
    public String toFileName(NameInfo info) {
        String name = shouldEncode() ? StringUtils.encodeToSha256(info.base()) : info.base();
        return name + (getSuffix().isEmpty() ? "" : "_" + getSuffix());
    }

    abstract String getLiteral();
    protected abstract String getPrefix();
    abstract String getSuffix();
    abstract boolean shouldEncode();
}