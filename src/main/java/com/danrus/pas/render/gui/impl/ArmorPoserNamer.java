package com.danrus.pas.render.gui.impl;

import com.danrus.pas.compat.armorposer.mixin.accessor.ArmorStandScreenAccessor;
import com.danrus.pas.mixin.accessors.ButtonAccessor;
import com.danrus.pas.render.gui.ArmorStandNamerAdapter;
import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public class ArmorPoserNamer implements ArmorStandNamerAdapter {

    private final ArmorStandScreen screen;

    public ArmorPoserNamer(ArmorStandScreen screen) {
        this.screen = screen;
    }

    @Override
    public void setNameInputValue(String value) {
        ( (ArmorStandScreenAccessor) screen).pas$getNameInput().setValue(value);
    }

    @Override
    public String getNameInputValue() {
        return ( (ArmorStandScreenAccessor) screen).pas$getNameInput().getValue();
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
}
