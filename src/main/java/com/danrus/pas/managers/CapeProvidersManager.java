package com.danrus.pas.managers;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.providers.MojangProvider;
import com.danrus.pas.impl.providers.cape.MinecraftCapesTextureProvider;
import com.danrus.pas.impl.providers.cape.NamemcCapeProvider;
import com.danrus.pas.impl.providers.common.AbstractTextureProviderManager;

public class CapeProvidersManager extends AbstractTextureProviderManager<CapeData> {

    private static final String DEFAULT_LITERAL = "M";
    private static final String EXCLUDE_LITERALS = "FI";

    @Override
    protected String getOutputString(NameInfo info) {
        return info.getFeature(CapeFeature.class).compile();
    }

    @Override
    protected void prepareProviders() {
        this.addProvider(MojangProvider.getInstance());
        this.addProvider(new MinecraftCapesTextureProvider(), 2);
        this.addProvider(new NamemcCapeProvider(), 1);
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(CapeFeature.class).getProvider();
    }

    @Override
    protected String getName() {
        return "CapeProvidersManager";
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
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }

    @Override
    protected boolean registerAsExistingProvider() {
        return false;
    }
}
