package com.danrus.pas.render.gui.widgets;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ButtonWithIcon extends Button {

    private static final WidgetSprites SPRITES = new net.minecraft.client.gui.components.WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted")
    );

    public ResourceLocation icon;


    public ButtonWithIcon(int x, int y, int width, int height, ResourceLocation icon, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ this.active ? SPRITES.enabled() : SPRITES.disabled(), this.getX(), this.getY(), this.getWidth(), this.getHeight()/*? >= 1.21.4 {*/, VersioningUtils.getARGBwhite(this.alpha)/*?}*/);
        int i = this.active ? 16777215 : 10526880;
        AbstractButton.renderScrollingString(guiGraphics, minecraft.font, this.getMessage(), this.getX() + 20, this.getY(), this.getX() + this.getWidth() - 2, this.getY() + this.getHeight(), i | Mth.ceil(this.alpha * 255.0F) << 24);
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ this.icon, this.getX() + 1, this.getY() + 1, 18, 18);

    }
}
