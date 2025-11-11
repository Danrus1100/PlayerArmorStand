package com.danrus.pas.impl.translators.skin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.translators.common.AbstractMojangTranslator;

public class MojangSkinTranslator extends AbstractMojangTranslator{

    @Override
    protected String getPrefix() {
        return "skins";
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    protected String getName(NameInfo info) {
        return info.base();
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(SkinProviderFeature.class).getProvider();
    }
}
