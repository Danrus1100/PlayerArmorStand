package com.danrus.pas.render;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfiguratorScreen extends Screen {

    private final Screen parent;

    public ConfiguratorScreen(Screen parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
