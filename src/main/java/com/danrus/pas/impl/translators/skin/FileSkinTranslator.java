package com.danrus.pas.impl.translators.skin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.impl.translators.common.AbstractFileTranslator;

public class FileSkinTranslator extends AbstractFileTranslator {
    @Override
    protected String getName(NameInfo info) {
        return info.base();
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(SkinProviderFeature.class).getProvider();
    }
}
