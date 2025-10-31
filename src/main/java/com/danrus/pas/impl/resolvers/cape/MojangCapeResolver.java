package com.danrus.pas.impl.resolvers.cape;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.resolvers.common.AbstractMojangResolver;

public class MojangCapeResolver extends AbstractMojangResolver {

    @Override
    public boolean isApplicable(NameInfo info) {
        return super.isApplicable(info) && info.getFeature(CapeFeature.class).isEnabled();
    }

    @Override
    protected String getPrefix() {
        return "cape";
    }
}
