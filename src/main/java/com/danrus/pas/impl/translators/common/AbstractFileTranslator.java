package com.danrus.pas.impl.translators.common;

import com.danrus.pas.api.info.NameInfo;

public abstract class AbstractFileTranslator extends AbstractSimpleTranslator{
    @Override
    protected String getLiteral() {
        return "F";
    }

    @Override
    protected String getPrefix() {
        return "randomfile";
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public boolean shouldEncode() {
        return false;
    }
}
