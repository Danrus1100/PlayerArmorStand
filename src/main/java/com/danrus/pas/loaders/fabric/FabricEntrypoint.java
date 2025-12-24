//? if fabric {
package com.danrus.pas.loaders.fabric;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.commands.PasCommandsRegistrar;
import com.danrus.pas.utils.Rl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerArmorStandsClient.initialize();

        FabricLoader.getInstance().getModContainer("pas").ifPresent(container -> {
            for (String packName : PlayerArmorStandsClient.RPS) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Rl.pas(packName),
                        container,
                        Component.translatable("pas.rp." + packName),
                        packName.equals(PlayerArmorStandsClient.DEFAULT_RP)
                                ? ResourcePackActivationType.DEFAULT_ENABLED
                                : ResourcePackActivationType.NORMAL
                );
            }
        });

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new PasCommandsRegistrar<FabricClientCommandSource>().register(dispatcher);
        } ));
    }
}
//?}
