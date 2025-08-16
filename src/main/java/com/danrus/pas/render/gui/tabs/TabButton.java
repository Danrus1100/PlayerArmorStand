package com.danrus.pas.render.gui.tabs;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class TabButton extends Button {

    private static final WidgetSprites TAB_BUTTON_SPRITES = new WidgetSprites(
            VersioningUtils.getResourceLocation("pas", "tab"),
            VersioningUtils.getResourceLocation("pas", "tab_selected"),
            VersioningUtils.getResourceLocation("pas", "tab_highlighted")
    );

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
        guiGraphics.blitSprite(RenderType::guiTextured, TAB_BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        this.renderString(guiGraphics, minecraft.font, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}