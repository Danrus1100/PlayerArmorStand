package com.danrus.pas.managers;

import com.danrus.pas.api.DataRepository;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.providers.MojangProvider;
import com.danrus.pas.impl.providers.cape.NamemcCapeProvider;
import com.danrus.pas.impl.providers.common.AbstractTextureProviderManager;

public class CapeProvidersManager extends AbstractTextureProviderManager<CapeData> {

    private static final String DEFAULT_LITERAL = "M";
    private static final String EXCLUDE_LITERALS = "F";

    @Override
    protected void prepareProviders() {
        this.addProvider(new MojangProvider());
        this.addProvider(new NamemcCapeProvider(), 1);
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
}
