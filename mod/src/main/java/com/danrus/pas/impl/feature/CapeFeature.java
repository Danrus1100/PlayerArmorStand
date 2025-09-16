package com.danrus.pas.impl.feature;

import com.danrus.pas.api.client.ClientLocationGetter;
import com.danrus.pas.api.feature.PasFeature;
import com.danrus.pas.core.pipeline.PasPipelineContext;
import com.danrus.pas.utils.StringUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class CapeFeature implements PasFeature {

    private boolean enabled = false;

    public CapeFeature(String string) {
        initFromString(string);
    }

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

    @Override
    public String getLocation(String baseName, String arguments) {
        return "pas:cape/" + StringUtils.encodeToSha256(baseName);
    }
}
