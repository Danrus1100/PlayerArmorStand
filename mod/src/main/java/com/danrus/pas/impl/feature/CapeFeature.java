package com.danrus.pas.impl.feature;

import com.danrus.pas.api.feature.IPasFeature;
import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;

public class CapeFeature implements IPasFeature {

    private boolean enabled = false;

    @Override
    public String getLiteral() {
        return "C";
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
        return false;
    }

    @Override
    public void apply(PasPipelineContext context) {
        context.setMetadata("cape", enabled);
    }

    @Override
    public void parse(String baseName, String arguments) {
        enabled = arguments.contains("C");
    }
}
