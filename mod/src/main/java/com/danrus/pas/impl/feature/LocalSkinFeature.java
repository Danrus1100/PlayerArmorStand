package com.danrus.pas.impl.feature;

import com.danrus.pas.api.feature.PasFeature;
import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;

public class LocalSkinFeature implements PasFeature {

    private String skinName = "";

    @Override
    public String getLiteral() {
        return "F";
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
        if (skinName.isEmpty()) return;
        context.setMetadata("local_skin", skinName);
    }

    @Override
    public void parse(String baseName, String arguments) {
        if (arguments.contains("F")) {
            this.skinName = baseName;
        }
    }

    @Override
    public String getLocation(String baseName, String arguments) {
        return "pas:random/" + baseName;
    }
}
