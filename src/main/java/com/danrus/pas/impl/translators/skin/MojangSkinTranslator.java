package com.danrus.pas.impl.translators.skin;

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
}
