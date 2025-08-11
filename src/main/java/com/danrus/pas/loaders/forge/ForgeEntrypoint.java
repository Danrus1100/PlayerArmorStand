//? if forge {
/*package com.danrus.pas.loaders.forge;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.config.ModConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("pas")
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        ModConfig.initialize();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, screen) -> ModConfig.getConfigScreen(screen)
                )
        );
        PlayerArmorStandsClient.initialize();
    }
}
*///?}
