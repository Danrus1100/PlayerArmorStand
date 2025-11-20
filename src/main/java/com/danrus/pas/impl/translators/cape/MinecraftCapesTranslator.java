package com.danrus.pas.impl.translators.cape;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.translators.common.AbstractSimpleTranslator;

public class MinecraftCapesTranslator extends AbstractSimpleTranslator {
    @Override
    protected String getLiteral() {
        return "I";
    }

    @Override
    protected String getPrefix() {
        return "mccapes";
    }

    @Override
    protected String getSuffix() {
        return "_mccpaes";
    }

    @Override
    protected String getName(NameInfo info) {
        return info.base();
    }

    @Override
    public boolean shouldEncode() {
        return true;
    }

    @Override
    protected String getProvider(NameInfo info) {
        return info.getFeature(CapeFeature.class).getProvider();
    }
}
