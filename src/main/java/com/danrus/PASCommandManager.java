package com.danrus;

import com.danrus.utils.ASModelData;
import com.danrus.utils.SkinsUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class PASCommandManager {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("pas").executes(PASCommandManager::defaultCommand)
                    .then(ClientCommandManager.literal("reload")
                            .then(ClientCommandManager.argument("name/skin", StringArgumentType.word()).executes(PASCommandManager::reloadSingeCommand)))
                    .then(ClientCommandManager.literal("reload_failed").executes(PASCommandManager::reloadFailedCommand)));
        }));
    }

    public static int defaultCommand(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.translatable("commands.pas.default_feedback"));
        return 1;
    }

    public static int reloadSingeCommand(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name/skin");
        PlayerArmorStands.modelDataCache.remove(name);
        PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, false, ASModelData.DownloadStatus.IN_PROGRESS));
        SkinsUtils.reloadSkinTexure(name);
        return 1;
    }

    public static int reloadFailedCommand(CommandContext<FabricClientCommandSource> context) {
        PlayerArmorStands.modelDataCache.forEach((name, asModelData) -> {
            if (asModelData.status == ASModelData.DownloadStatus.FAILED) {
                PlayerArmorStands.modelDataCache.remove(name);
                PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, false, ASModelData.DownloadStatus.IN_PROGRESS));
                SkinsUtils.getSkinTexture(name);
            }
        });
        return 1;
    }
}
