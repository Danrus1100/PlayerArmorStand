package com.danrus.pas.api.feature;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;

public interface PasFeature {
    String getLiteral();

    List<String> getMandatoryArguments();
    List<String> getOptionalArguments();
    boolean isSourceHint();

    void apply(PasPipelineContext context);
    void parse(String baseName, String arguments);

    default void initFromString(String string) {
        String[] parts = string.split("\\|", 2);
        parse(parts[0].trim(), parts.length > 1 ? parts[1].trim() : "");
    }

    // Just an alias
    static PasFeature[] of(String string) {
        return PasApi.getFeatureRegistry().createFeatures(string);
    }

    static String resolveSourceHint(PasFeature[] features) {
        for (PasFeature feature : features) {
            if (feature.isSourceHint()) {
                return feature.getLiteral();
            }
        }
        return "";
    }
}