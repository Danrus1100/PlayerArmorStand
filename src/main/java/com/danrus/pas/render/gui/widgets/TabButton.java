package com.danrus.pas.render.gui.widgets;

import com.danrus.pas.utils.mc.ModUtils;
import com.danrus.pas.utils.mc.Id;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TabButton extends Button {

    private static final WidgetSprites TAB_BUTTON_SPRITES = new net.minecraft.client.gui.components.WidgetSprites(
            Id.pas("tab"),
            Id.pas("tab_selected"),
            Id.pas("tab_highlighted")
    );

    public OnPress tabOnPress;

    public TabButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, (button) -> {}, DEFAULT_NARRATION);
    }


    @Override
    //? <1.21.9
    //public void onPress() {
    //? >=1.21.9
    public void onPress(net.minecraft.client.input.InputWithModifiers inputWithModifiers) {
        if (this.tabOnPress != null) {
            this.tabOnPress.onPress(this);
        }
    }

    @Override
    protected void
    //? <1.21.11
    //renderWidget
    //? >=1.21.11 && <26.1
    //renderContents
    //? >=26.1
    extractContents
    (GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TAB_BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight() /*? >= 1.21.4 {*/, ModUtils.getARGBwhite(this.alpha)/*?}*/);

        //? <1.21.11
        //this.renderString(guiGraphics, minecraft.font, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
        //? =1.21.11
        //this.renderScrollingStringOverContents(guiGraphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE), this.getMessage(), 2);
        //? >=26.1
        this.extractScrollingStringOverContents(guiGraphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE), this.getMessage(), 2);
    }
}