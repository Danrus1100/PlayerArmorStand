package com.danrus.pas.impl.translators.cape;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.translators.common.AbstractNamemcTranslator;

public class NamemcCapeTranslator extends AbstractNamemcTranslator {
    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    protected String getName(NameInfo info) {
        return info.getFeature(CapeFeature.class).getId();
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(CapeFeature.class).getProvider();
    }

    @Override
    protected String getPrefix() {
        return "cape";
    }

    @Override
    protected String getLiteral() {
        return "A";
    }
}
