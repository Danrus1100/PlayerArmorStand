//? if fabric {
package com.danrus.pas.loaders.fabric;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfig.initialize();
        PlayerArmorStandsClient.initialize();
    }
}
//?}
