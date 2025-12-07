//? if fabric {
package com.danrus.pas.loaders.fabric;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.commands.PasCommandsRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerArmorStandsClient.initialize();
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new PasCommandsRegistrar<FabricClientCommandSource>().register(dispatcher);
        } ));
    }
}
//?}
