package com.danrus.pas.impl.feature;

import com.danrus.pas.api.feature.IPasFeature;
import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;

public class NamemcFeature implements IPasFeature {

    private String skinId = "";

    @Override
    public String getLiteral() {
        return "N";
    }

    @Override
    public List<String> getMandatoryArguments() {
        return List.of();
    }

    @Override
    public List<String> getOptionalArguments() {
        return List.of();
    }

    @Override
    public boolean isSourceHint() {
        return true;
    }

    @Override
    public void apply(PasPipelineContext context) {
        if (skinId.isEmpty()) return;
        context.setMetadata("fetch_link", "https://namemc.com/skin/" + skinId + ".png");
    }

    @Override
    public void parse(String baseName, String arguments) {
        if (arguments.contains("N")) {
            this.skinId = baseName;
        }
    }
}
