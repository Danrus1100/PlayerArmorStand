package com.danrus.pas.render.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class EnterEditBox extends EditBox {

    private final OnEnterPressed onEnterPressed;

    //? if >= 1.21.1 {
    public EnterEditBox(Font font, int width, int height, Component message, OnEnterPressed onEnterPressed) {
        super(font, width, height, message);
        this.onEnterPressed = onEnterPressed;
    }
    //?}

    public EnterEditBox(Font font, int x, int y, int width, int height, Component message, OnEnterPressed onEnterPressed) {
        super(font, x, y, width, height, message);
        this.onEnterPressed = onEnterPressed;
    }

    public EnterEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message, OnEnterPressed onEnterPressed) {
        super(font, x, y, width, height, editBox, message);
        this.onEnterPressed = onEnterPressed;
    }

    @Override
    //? <1.21.9 {
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    //?} else {
    /*public boolean keyPressed(net.minecraft.client.input.KeyEvent keyEvent) {
        int keyCode = keyEvent.key();
    *///?}

        if (keyCode == 257 || keyCode == 335) { // Enter or Keypad Enter
            this.onEnterPressed.onEnterPressed(this);
            return true;
        }
        //? <1.21.9 {
        return super.keyPressed(keyCode, scanCode, modifiers);
        //?} else {
        /*return super.keyPressed(keyEvent);
        *///?}
    }

    public interface OnEnterPressed {
        void onEnterPressed(EnterEditBox editBox);
    }
}
