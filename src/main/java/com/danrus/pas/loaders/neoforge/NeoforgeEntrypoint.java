//? if neoforge {
/*package com.danrus.pas.loaders.neoforge;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.commands.PasCommands;
import com.danrus.pas.commands.SkinDataArgument;
import com.danrus.pas.config.YaclConfig;
import com.danrus.pas.render.gui.DummyConfigScreen;
import com.danrus.pas.utils.ModUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.VanillaPackResourcesBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ClientCommandHandler;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Mod("pas")
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint() {
        PlayerArmorStandsClient.initialize();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parent) -> {
                    //? if yacl {
                    if (ModUtils.isModLoaded("yet_another_config_lib_v3")) {
                                    return YaclConfig.getConfigScreen(parent);
                                }
                    //?}
                    return new DummyConfigScreen(parent);
                }
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
            );
        });
    }

    @SubscribeEvent
    private void onAddPackFinders(AddPackFindersEvent event) {
//        try {
//            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
//                var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("resourcepacks/anti_tropophobia");
//                var pack = new PathPackResources(ModList.get().getModFileById(MODID).getFile().getFileName() + ":" + resourcePath, resourcePath);
//                var metadataSection = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
//                if (metadataSection != null) {
//                    event.addRepositorySource((packConsumer, packConstructor) ->
//                            packConsumer.accept(packConstructor.create(
//                                    "builtin/the_bumblezone", Component.literal("Bumblezone - Anti Trypophobia"), false,
//                                    () -> pack, metadataSection, Pack.Position.BOTTOM, PackSource.BUILT_IN, false)));
//                }
//            }
//        }
//        catch(IOException ex) {
//            throw new RuntimeException(ex);
//        }
        try {
            if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
            for (String packName : PlayerArmorStandsClient.RPS) {
                IModFile modFile = ModList.get().getModFileById(PlayerArmorStandsClient.MOD_ID).getFile();
                Path resourcePath = modFile.findResource("resourcepacks/" + packName);
                var pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, resourcePath);
            }
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
*///?}
