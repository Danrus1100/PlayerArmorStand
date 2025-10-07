package com.danrus.pas.commands;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.managers.PasManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PasCommands {

    public static final List<String> COMMANDS_NAMES = List.of("player-armor-stands", "pas");

    public static int reloadSingeCommand(CommandContext<?> context) {
        SkinData data = SkinDataArgument.getData(context, "name/skin");
        data.setStatus(DownloadStatus.IN_PROGRESS);
        String name = data.getParams().isEmpty() ? data.getName() : data.getName() + "|" + data.getParams();
        PasManager.getInstance().reloadData(name);
        return 1;
    }

    public static int reloadFailedCommand(CommandContext<?> context) {
        PasManager.getInstance().reloadFailed();
        return 1;
    }

    public static Component defaultCommand() {
        return Component.translatable("commands.pas.default_feedback");
    }
}
