package com.danrus.pas;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.client.event.PasClientInitEvent;
//import com.danrus.pas.utils.managers.CommandsManager;
import com.danrus.pas.impl.feature.CapeFeature;
import com.danrus.pas.impl.feature.NamemcFeature;
import com.danrus.pas.impl.feature.OverlayFeature;
import com.danrus.pas.impl.feature.SlimFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static void initialize() {
        PasApi.postEvent(new PasClientInitEvent());
//        CommandsManager.init();
    }

    public static void registerFeatures() {
        PasApi.getFeatureRegistry().registerFeature(CapeFeature.class, CapeFeature::new);
        PasApi.getFeatureRegistry().registerFeature(NamemcFeature.class, NamemcFeature::new);
        PasApi.getFeatureRegistry().registerFeature(OverlayFeature.class, OverlayFeature::new);
        PasApi.getFeatureRegistry().registerFeature(SlimFeature.class, SlimFeature::new);
    }
}
