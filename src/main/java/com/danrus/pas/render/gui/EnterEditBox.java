package com.danrus.pas.render.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class EnterEditBox extends EditBox {

    private final OnEnterPressed onEnterPressed;

    public EnterEditBox(Font font, int width, int height, Component message, OnEnterPressed onEnterPressed) {
        super(font, width, height, message);
        this.onEnterPressed = onEnterPressed;
    }

    public EnterEditBox(Font font, int x, int y, int width, int height, Component message, OnEnterPressed onEnterPressed) {
        super(font, x, y, width, height, message);
        this.onEnterPressed = onEnterPressed;
    }

    public EnterEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message, OnEnterPressed onEnterPressed) {
        super(font, x, y, width, height, editBox, message);
        this.onEnterPressed = onEnterPressed;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter or Keypad Enter
            this.onEnterPressed.onEnterPressed(this);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public interface OnEnterPressed {
        void onEnterPressed(EnterEditBox editBox);
    }
}
