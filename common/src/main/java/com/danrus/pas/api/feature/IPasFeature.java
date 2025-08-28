package com.danrus.pas.api.feature;

import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;

public interface IPasFeature {
    String getLiteral();

    List<String> getMandatoryArguments();
    List<String> getOptionalArguments();
    boolean isSourceHint();

    void apply(PasPipelineContext context);
    void parse(String baseName, String arguments);
}