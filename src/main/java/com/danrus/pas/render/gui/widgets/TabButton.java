package com.danrus.pas.render.gui.widgets;

import com.danrus.pas.utils.ModUtils;
import com.danrus.pas.utils.Rl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TabButton extends Button {

    private static final WidgetSprites TAB_BUTTON_SPRITES = new net.minecraft.client.gui.components.WidgetSprites(
            Rl.pas("tab"),
            Rl.pas("tab_selected"),
            Rl.pas("tab_highlighted")
    );

    public OnPress tabOnPress;

    public TabButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, (button) -> {}, DEFAULT_NARRATION);
    }


    @Override
    //? <1.21.9
    public void onPress() {
    //? >=1.21.9
    /*public void onPress(net.minecraft.client.input.InputWithModifiers inputWithModifiers) {*/
        if (this.tabOnPress != null) {
            this.tabOnPress.onPress(this);
        }
    }

    @Override
    protected void
    //? <1.21.11
    renderWidget
    //? >=1.21.11
    /*renderContents*/
    (GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        //? >= 1.21.1 {
        guiGraphics.blitSprite(/*? >= 1.21.4 {*/ModUtils.getGuiRender(),/*?}*/ TAB_BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight() /*? >= 1.21.4 {*/, ModUtils.getARGBwhite(this.alpha)/*?}*/);
        //?} else {
        /*guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TAB_LOCATION, this.getX(), this.getY(), 0, 0, 80, 15, 80, 15);
        *///?}

        //? <1.21.11 {
        this.renderString(guiGraphics, minecraft.font, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
        //?} else {
        /*this.renderScrollingStringOverContents(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE), this.getMessage(), 2);
        *///?}
    }
}