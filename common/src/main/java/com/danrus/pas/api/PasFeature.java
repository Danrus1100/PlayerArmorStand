package com.danrus.pas.api;

import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.ArrayList;
import java.util.List;

public interface PasFeature {
    String getLiteral();

    List<String> getMandatoryArguments();
    List<String> getOptionalArguments();
    boolean isSourceHint();

    void apply(PasPipelineContext context);
    void parse(String argument);
}