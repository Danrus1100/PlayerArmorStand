package com.danrus.pas.impl.translators.skin;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.translators.common.AbstractNamemcTranslator;

public class NamemcSkinTranslator extends AbstractNamemcTranslator {

    @Override
    protected String getPrefix() {
        return "skins";
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(SkinProviderFeature.class).getProvider();
    }
}
