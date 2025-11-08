package com.danrus.pas.impl.translators.cape;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.translators.common.AbstractMojangTranslator;

public class MojangCapeTranslator extends AbstractMojangTranslator {

    @Override
    protected String getPrefix() {
        return "capes";
    }

    @Override
    protected String getSuffix() {
        return "_cape";
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(CapeFeature.class).getProvider();
    }
}
