package com.danrus.pas.render.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class DummyConfigScreen extends Screen {

    private final Screen parent;

    public DummyConfigScreen(Screen parent) {
        super(Component.literal("Yacl Required!"));
        this.parent = parent;
    }

    @Override
    //~ screen_render
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        Font font = Minecraft.getInstance().font;
        guiGraphics.centeredText(font, Component.translatable("pas.yacl_required.1"), width/2, height/2, Color.WHITE.getRGB());
        guiGraphics.centeredText(font, Component.translatable("pas.yacl_required.2"), width/2, height/2+font.lineHeight+2, Color.WHITE.getRGB());
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }
}
