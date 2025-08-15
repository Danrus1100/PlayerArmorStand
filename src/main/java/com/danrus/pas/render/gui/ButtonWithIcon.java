package com.danrus.pas.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonWithIcon extends Button {

    public ResourceLocation icon;

    protected ButtonWithIcon(int x, int y, int width, int height, ResourceLocation icon, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blitSprite(RenderType::guiTextured, this.icon, this.getX(), this.getY(), 20, 20);
    }
}
