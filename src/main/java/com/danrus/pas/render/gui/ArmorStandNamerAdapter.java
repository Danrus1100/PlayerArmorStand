package com.danrus.pas.render.gui;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

public interface ArmorStandNamerAdapter {
    void setNameInputValue(String value);
    String getNameInputValue();
    Screen getScreen();
}
