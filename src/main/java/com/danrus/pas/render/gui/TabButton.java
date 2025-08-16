package com.danrus.pas.render.gui;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.List;

public class TabButton extends Button {

    private static final WidgetSprites TAB_BUTTON_SPRITES = new WidgetSprites(
            VersioningUtils.getResourceLocation("pas", "tab"),
            VersioningUtils.getResourceLocation("pas", "tab_selected"),
            VersioningUtils.getResourceLocation("pas", "tab_highlighted")
    );

    private final List<TabButton> tabButtons;

    public TabButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        this(x, y, width, height, message, onPress, (TabButton[]) null);
    }

    public TabButton(int x, int y, int width, int height, Component message, OnPress onPress, TabButton... tabButtons) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.tabButtons = List.of(tabButtons);
    }

    public void addTabButton(TabButton tabButton) {
        this.tabButtons.add(tabButton);
    }

    @Override
    public void onPress() {
        tabButtons.forEach(button -> button.active = true);
        this.active = false;
        super.onPress();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(RenderType::guiTextured, TAB_BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
