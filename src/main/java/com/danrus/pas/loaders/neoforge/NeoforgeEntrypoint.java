//? if neoforge {
/*package com.danrus.pas.loaders.neoforge;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.config.ModConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("pas")
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint() {
        PlayerArmorStandsClient.initialize();
        ModConfig.initialize();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parent) -> ModConfig.getConfigScreen(parent)
        );
    }
}
*///?}
