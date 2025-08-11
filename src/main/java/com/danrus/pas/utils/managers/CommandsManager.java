package com.danrus.pas.utils.managers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.minecraft.network.chat.Component;

public class CommandsManager {
    public static void init() {
        ClientCommandRegistrationEvent.EVENT.register(((dispatcher, context) ->
                dispatcher.register(ClientCommandRegistrationEvent.literal("player-armor-stands").executes(CommandsManager::defaultCommand)
                .then(ClientCommandRegistrationEvent.literal("reload_failed").executes(CommandsManager::reloadFailedCommand))
                .then(ClientCommandRegistrationEvent.literal("reload").then(ClientCommandRegistrationEvent.argument("name/skin", StringArgumentType.word()).executes(CommandsManager::reloadSingeCommand)))
                )
        ));
    }

    private static int reloadSingeCommand(CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name/skin");
        if (name.isEmpty()) {
            context.getSource().arch$sendFailure(Component.translatable("commands.pas.reload_failed_error"));
            return 0;
        }
        SkinManger.getInstance().reloadData(name);
        return 1;
    }

    private static int reloadFailedCommand(CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context) {
        SkinManger.getInstance().reloadFailed();
        return 1;
    }

    private static int defaultCommand(CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context) {
        context.getSource().arch$sendFailure(Component.translatable("commands.pas.default_feedback"));
        return 1;
    }
}
