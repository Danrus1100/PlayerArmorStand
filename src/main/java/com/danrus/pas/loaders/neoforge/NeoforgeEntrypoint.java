//? if neoforge {
/*package com.danrus.pas.loaders.neoforge;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.commands.PasCommands;
import com.danrus.pas.utils.commands.SkinDataArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ClientCommandHandler;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

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

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        PasCommands.COMMANDS_NAMES.forEach(s -> {
            dispatcher.register(
                    LiteralArgumentBuilder.<CommandSourceStack>literal(s)
                            .executes(a -> {
                                a.getSource().sendFailure(PasCommands.defaultCommand());
                                return 1;
                            })
                            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("reload_failed")
                                    .executes(PasCommands::reloadFailedCommand))
                            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("reload")
                                    .then(RequiredArgumentBuilder.<CommandSourceStack, SkinData>argument(
                                                    "name/skin", new SkinDataArgument()
                                            ).executes(PasCommands::reloadSingeCommand)
                                    )
                            )
            );
        });
    }
}
*///?}
