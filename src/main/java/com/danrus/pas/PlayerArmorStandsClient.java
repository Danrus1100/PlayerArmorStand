package com.danrus.pas;

import com.danrus.pas.api.FeatureRegistry;
import com.danrus.pas.impl.features.*;
import com.danrus.pas.managers.PasManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    //? >=1.21.9
    /*public static ModelPart capeDef = com.danrus.pas.render.armorstand.PasCapeModel.createCapeLayer().bakeRoot();*/

    public static void initialize() {
        PasManager.getInstance();
        FeatureRegistry reg = FeatureRegistry.getInstance();
        reg.register(OverlayFeature.class);
        reg.register(CapeFeature.class);
        reg.register(SkinProviderFeature.class);
        reg.register(SlimFeature.class);
        reg.register(DisplayNameFeature.class);
    }
}
