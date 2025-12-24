//? if neoforge {
/*package com.danrus.pas.loaders.neoforge;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.commands.PasCommands;
import com.danrus.pas.commands.PasCommandsRegistrar;
import com.danrus.pas.commands.SkinDataArgument;
import com.danrus.pas.config.YaclConfig;
import com.danrus.pas.render.gui.DummyConfigScreen;
import com.danrus.pas.utils.ModUtils;
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
        new PasCommandsRegistrar<CommandSourceStack>().register(dispatcher);
    }
}
*///?}
