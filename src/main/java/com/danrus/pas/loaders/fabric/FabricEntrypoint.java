//? if fabric {
package com.danrus.pas.loaders.fabric;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.commands.PasCommands;
import com.danrus.pas.commands.SkinDataArgument;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfig.initialize();
        PlayerArmorStandsClient.initialize();
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            PasCommands.COMMANDS_NAMES.forEach(s -> {
                dispatcher.register(
                        ClientCommandManager.literal(s).executes((a) -> {
                            a.getSource().sendError(PasCommands.defaultCommand());
                            return 1;
                        })

                                .then(ClientCommandManager.literal("reload_failed").executes(PasCommands::reloadFailedCommand))
                                .then(ClientCommandManager.literal("reload").then(ClientCommandManager.argument("name/skin", new SkinDataArgument()).executes(PasCommands::reloadSingeCommand)))
                                .then(ClientCommandManager.literal("debug")
                                        .then(ClientCommandManager.literal("drop_cache").executes(PasCommands::dropCacheCommand))
                                )
                );
            });
        } ));
    }
}
//?}
