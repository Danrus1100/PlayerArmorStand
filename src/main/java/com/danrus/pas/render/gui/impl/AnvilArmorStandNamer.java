package com.danrus.pas.render.gui.impl;

import com.danrus.pas.mixin.accessors.AnvilScreenAccessor;
import com.danrus.pas.render.gui.ArmorStandNamerAdapter;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;

public class AnvilArmorStandNamer implements ArmorStandNamerAdapter {

    private final AnvilScreen screen;

    public AnvilArmorStandNamer(AnvilScreen screen) {
        this.screen = screen;
    }

    @Override
    public void setNameInputValue(String value) {
        ( (AnvilScreenAccessor) this.screen).pas$getNameInput().setValue(value);
    }

    @Override
    public String getNameInputValue() {
        return ( (AnvilScreenAccessor) this.screen).pas$getNameInput().getValue();
    }

    @Override
    public Screen getScreen() {
        return this.screen;
    }
}
