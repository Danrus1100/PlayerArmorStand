package com.danrus.pas.render.gui;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ButtonWithIcon extends Button {

    //? >= 1.21.1 {
    private static final net.minecraft.client.gui.components.WidgetSprites SPRITES = new net.minecraft.client.gui.components.WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted")
    );
    //?} else {
    /*private static final ResourceLocation WIDGETS_LOCATION = VersioningUtils.getResourceLocation("minecraft", "textures/gui/widgets.png");
    *///?}

    public ResourceLocation icon;


    protected ButtonWithIcon(int x, int y, int width, int height, ResourceLocation icon, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        //? >= 1.21.1 {
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ this.active ? SPRITES.enabled() : SPRITES.disabled(), this.getX(), this.getY(), this.getWidth(), this.getHeight()/*? >= 1.21.4 {*/, VersioningUtils.getARGBwhite(this.alpha)/*?}*/);
        //?} else {
        /*guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, getTextureY());
        *///?}
        int i = this.active ? 16777215 : 10526880;
        AbstractButton.renderScrollingString(guiGraphics, minecraft.font, this.getMessage(), this.getX() + 20, this.getY(), this.getX() + this.getWidth() - 2, this.getY() + this.getHeight(), i | Mth.ceil(this.alpha * 255.0F) << 24);
        //? >= 1.21.1 {
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ this.icon, this.getX() + 1, this.getY() + 1, 18, 18);
        //?} else {
        /*guiGraphics.blit(this.icon, this.getX() + 1, this.getY() + 1, 18, 18, 0, 0, 18, 18, 18, 18);
        *///?}

    }

    //? if <= 1.20.1 {
    /*private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }
    *///?}
}
