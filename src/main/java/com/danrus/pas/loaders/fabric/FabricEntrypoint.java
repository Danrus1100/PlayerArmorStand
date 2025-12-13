//? if fabric {
package com.danrus.pas.loaders.fabric;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.commands.CapeDataArgument;
import com.danrus.pas.commands.PasCommands;
import com.danrus.pas.commands.SkinDataArgument;
import com.danrus.pas.utils.Rl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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
            PasCommands.COMMANDS_NAMES.forEach(s -> {
                dispatcher.register(
                        ClientCommandManager.literal(s).executes((a) -> {
                            a.getSource().sendError(PasCommands.defaultCommand());
                            return 1;
                        })

                                .then(ClientCommandManager.literal("reload_failed").executes(PasCommands::reloadFailedCommand))
                                .then(ClientCommandManager.literal("reload")
                                        .then(ClientCommandManager.literal("skin")
                                                .then(ClientCommandManager.argument("name/skin", new SkinDataArgument()).executes(PasCommands::reloadSingeSkinCommand)))

                                        .then(ClientCommandManager.literal("cape")
                                                .then(ClientCommandManager.argument("name/cape", new CapeDataArgument()).executes(PasCommands::reloadSingleCapeCommand)))
                                )
                                .then(ClientCommandManager.literal("debug")
                                        .then(ClientCommandManager.literal("drop_cache").executes(PasCommands::dropCacheCommand))
                                )
                );
            });
        } ));
    }
}
//?}
