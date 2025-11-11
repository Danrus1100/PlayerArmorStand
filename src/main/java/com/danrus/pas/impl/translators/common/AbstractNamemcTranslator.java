    package com.danrus.pas.impl.translators.common;

    public abstract class AbstractNamemcTranslator extends AbstractSimpleTranslator {

        @Override
        protected String getSuffix() {
            return "namemc";
        }

        @Override
        boolean shouldEncode() {
            return false; // NameMC использует обычные имена
        }
    }
