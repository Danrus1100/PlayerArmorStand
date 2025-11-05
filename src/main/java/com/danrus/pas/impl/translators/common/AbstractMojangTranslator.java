package com.danrus.pas.impl.translators.common;

public abstract class AbstractMojangTranslator extends AbstractSimpleTranslator {

    @Override
    String getLiteral() {
        return "M";
    }

    @Override
    boolean shouldEncode() {
        return true;
    }
}
