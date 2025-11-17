package com.danrus.pas.managers;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.providers.MojangProvider;
import com.danrus.pas.impl.providers.common.AbstractTextureProviderManager;
import com.danrus.pas.impl.providers.skin.NamemcSkinProvider;

public class SkinProvidersManager extends AbstractTextureProviderManager<SkinData> {

    private static final String DEFAULT_LITERAL = "M";
    private static final String EXCLUDE_LITERALS = "NF";

    @Override
    protected String getOutputString(NameInfo info) {
        return info.base();
    }

    @Override
    protected void prepareProviders() {
        this.addProvider(MojangProvider.getInstance());
        this.addProvider(new NamemcSkinProvider(), 1);
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(SkinProviderFeature.class).getProvider();
    }

    @Override
    protected String getName() {
        return "SkinProvidersManager";
    }

    @Override
    protected String getDefaultLiteral() {
        return DEFAULT_LITERAL;
    }

    @Override
    protected String getExcludeLiterals() {
        return EXCLUDE_LITERALS;
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }
}
