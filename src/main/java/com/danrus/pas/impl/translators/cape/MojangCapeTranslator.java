package com.danrus.pas.impl.translators.cape;

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
}
