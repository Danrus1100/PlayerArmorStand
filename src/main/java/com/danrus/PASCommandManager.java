package com.danrus;

//import com.danrus.utils.PASModelData;
//import com.danrus.utils.skin.SkinsUtils;
import com.danrus.utils.data.DataManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class PASCommandManager {
     public static void register() {
         ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
             dispatcher.register(ClientCommandManager.literal("player-armor-stands").executes(PASCommandManager::defaultCommand)
                     .then(ClientCommandManager.literal("reload_failed").executes(PASCommandManager::reloadFailedCommand))
                     .then(ClientCommandManager.literal("reload")
                             .then(ClientCommandManager.argument("name/skin", StringArgumentType.word()).executes(PASCommandManager::reloadSingeCommand))));
//                     .then(ClientCommandManager.literal("reset")
//                             .then(ClientCommandManager.argument("name/skin", StringArgumentType.word()).executes(PASCommandManager::resetCommand))));
         }));
     }

    public static int defaultCommand(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.translatable("commands.pas.default_feedback"));
        return 1;
    }

    public static int reloadSingeCommand(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name/skin");
//        PASClient.modelDataCache.remove(name);
//        PASClient.modelDataCache.put(name, new PASModelData(name));
//        SkinsUtils.reloadSkinTexure(name);
//        DataManager dataManager = SkinManger.getInstance().getDataManager();
//        dataManager.getData(name).setSkinTexture(PASModelData.DEFAULT_TEXTURE);
//        dataManager.getData(name).setCapeTexture(PASModelData.DEFAULT_CAPE);
        SkinManger.getInstance().reloadData(name);
        return 1;
    }

    public static int reloadFailedCommand(CommandContext<FabricClientCommandSource> context) {
//        PASClient.modelDataCache.forEach((name, asModelData) -> {
//            if (asModelData.status.isFailed()) {
//                PASClient.modelDataCache.remove(name);
//                PASClient.modelDataCache.put(name, new PASModelData(name));
//                SkinsUtils.getSkinTexture(name);
//            }
//        });
        SkinManger.getInstance().reloadFailed();
        return 1;
    }

//    public static int resetCommand(CommandContext<FabricClientCommandSource> context) {
//        String name = StringArgumentType.getString(context, "name/skin");
//        PASClient.modelDataCache.remove(name);
//        return 1;
//    }
}
