package com.danrus.pas.impl.resolvers.common;

public abstract class AbstractMojangResolver extends AbstractSimpleResolver{

    @Override
    String getLiteral() {
        return "M";
    }

    @Override
    String getSuffix() {
        return "";
    }

    @Override
    boolean shouldEncode() {
        return true;
    }
}
