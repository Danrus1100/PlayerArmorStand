    package com.danrus.pas.impl.translators.common;

    public abstract class AbstractNamemcTranslator extends AbstractSimpleTranslator {

        @Override
        protected String getSuffix() {
            return "namemc";
        }

        @Override
        public boolean shouldEncode() {
            return false;
        }
    }
