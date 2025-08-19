package com.danrus.pas;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.client.PasClientInitEvent;
//import com.danrus.pas.utils.managers.CommandsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static void initialize() {
        PasApi.postEvent(new PasClientInitEvent());
//        CommandsManager.init();
    }
}
