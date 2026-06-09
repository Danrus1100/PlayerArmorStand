package com.danrus.pas.commands;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PasCommands {

    public static final List<String> COMMANDS_NAMES = List.of("player-armor-stands", "pas");

    public static int reloadSingeSkinCommand(CommandContext<?> context) {
        NameInfo info = NameInfoArgumentType.getNameInfo(context, "name/skin");
        PasManager.getInstance().reloadData(info, SkinData.class);
        return 1;
    }

    public static int reloadSingleCapeCommand(CommandContext<?> context) {
        NameInfo info = NameInfoArgumentType.getNameInfo(context, "name/cape");
        PasManager.getInstance().reloadData(info, CapeData.class);
        return 1;
    }

    public static int reloadFailedCommand(CommandContext<?> context) {
        PasManager.getInstance().reloadFailed();
        return 1;
    }

    public static int dropCacheCommand(CommandContext<?> context) {
        PasManager.getInstance().dropCache();
        return 1;
    }

    public static Component defaultCommand() {
        return Component.translatable("commands.pas.default_feedback");
    }
}
