package com.danrus.pas.impl.translators.common;

public abstract class AbstractMojangTranslator extends AbstractSimpleTranslator {

    @Override
    protected String getLiteral() {
        return "M";
    }

    @Override
    public boolean shouldEncode() {
        return true;
    }
}
