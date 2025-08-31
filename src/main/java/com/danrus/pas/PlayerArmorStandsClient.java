package com.danrus.pas;

import com.danrus.pas.render.gui.tooltip.PasClientTooltipComponent;
import com.danrus.pas.render.gui.tooltip.PasTooltipComponent;
import com.danrus.pas.utils.managers.CommandsManager;
import dev.architectury.registry.client.gui.ClientTooltipComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static void initialize() {
        ClientTooltipComponentRegistry.register(PasTooltipComponent.class, PasClientTooltipComponent::new);
        CommandsManager.init();
    }
}
