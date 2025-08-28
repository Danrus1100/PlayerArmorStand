package com.danrus.pas.utils.managers;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.utils.commands.SkinDataArgument;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CommandsManager {

    private static final List<String> COMMANDS_NAMES = List.of("player-armor-stands", "pas");

    public static void init() {
        COMMANDS_NAMES.forEach((command_name) -> {
            ClientCommandRegistrationEvent.EVENT.register(((dispatcher, context) ->
                    dispatcher.register(ClientCommandRegistrationEvent.literal(command_name).executes(CommandsManager::defaultCommand)
                            .then(ClientCommandRegistrationEvent.literal("reload_failed").executes(CommandsManager::reloadFailedCommand))
                            .then(ClientCommandRegistrationEvent.literal("reload").then(ClientCommandRegistrationEvent.argument("name/skin", new SkinDataArgument()).executes(CommandsManager::reloadSingeCommand)))
                    )
            ));
        });
    }

    private static int reloadSingeCommand(CommandContext<ClientCommandRegistrationEvent.ClientCommandSourceStack> context) {
        SkinData data = SkinDataArgument.getData(context, "name/skin");
        data.setStatus(DownloadStatus.IN_PROGRESS);
        String name = data.getParams().isEmpty() ? data.getName() : data.getName() + "|" + data.getParams();
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
