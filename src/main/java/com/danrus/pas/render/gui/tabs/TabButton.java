package com.danrus.pas.render.gui.tabs;

import com.danrus.pas.utils.VersioningUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TabButton extends Button {

    //? >= 1.21.1 {
    private static final net.minecraft.client.gui.components.WidgetSprites TAB_BUTTON_SPRITES = new WidgetSprites(
            VersioningUtils.getResourceLocation("pas", "tab"),
            VersioningUtils.getResourceLocation("pas", "tab_selected"),
            VersioningUtils.getResourceLocation("pas", "tab_highlighted")
    );
    //?} else {
    /*private static final ResourceLocation TAB_LOCATION = VersioningUtils.getGuiLocation("pas", "tab");
    *///?}

    public OnPress tabOnPress;

    public TabButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, (button) -> {}, DEFAULT_NARRATION);
    }

    @Override
    public void onPress() {
        if (this.tabOnPress != null) {
            this.tabOnPress.onPress(this);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        //? >= 1.21.1 {
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ TAB_BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight() /*? >= 1.21.4 {*/, VersioningUtils.getARGBwhite(this.alpha)/*?}*/);
        //?} else {
        /*guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(TAB_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.height);
        *///?}
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderString(guiGraphics, minecraft.font, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}