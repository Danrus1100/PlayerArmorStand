package com.danrus.pas.render.common;

import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;

public record PasModelSettings(
        PasModelPoseSettings poseSettings,
        boolean slim,
        boolean foil
) {
    public static final PasModelSettings DEFAULT = new PasModelSettings(new PasModelPoseSettings(), false, false);
}
