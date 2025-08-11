package com.danrus.pas;

import com.danrus.pas.utils.managers.CommandsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static void initialize() {
        CommandsManager.init();
    }
}
