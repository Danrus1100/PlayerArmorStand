package com.danrus.pas.impl.feature;

import com.danrus.pas.api.feature.PasFeature;
import com.danrus.pas.core.pipeline.PasPipelineContext;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OverlayFeature implements PasFeature {

    private String overlayId = "";
    private String blend = "100";

    public OverlayFeature(String string) {
        initFromString(string);
    }

    @Override
    public String getLiteral() {
        return "T";
    }

    @Override
    public List<String> getMandatoryArguments() {
        return List.of(overlayId);
    }

    @Override
    public List<String> getOptionalArguments() {
        return List.of(blend);
    }

    @Override
    public boolean isSourceHint() {
        return false;
    }

    @Override
    public void apply(PasPipelineContext context) {
        if (overlayId.isEmpty()) return;
        context.setMetadata("overlay_id", overlayId);
        context.setMetadata("overlay_blend", Integer.parseInt(blend));
    }

    @Override
    public void parse(String baseName, String arguments) {
        String regex = "^T:([a-z0-9_]+)(?:%([0-9]{1,3}))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(arguments);

        if (matcher.matches()) {
            overlayId = matcher.group(1);
            blend = matcher.groupCount() > 1 && matcher.group(2) != null
                    ? matcher.group(2)
                    : "100";
        } else {
            // TODO: Overlay message of error
            overlayId = "";
            blend = "100";
        }
    }
}
