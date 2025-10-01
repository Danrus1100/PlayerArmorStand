package com.danrus.pas.render.gui.impl;

//? if armorposer {

import com.danrus.pas.compat.armorposer.mixin.accessor.ArmorStandScreenAccessor;
import com.danrus.pas.mixin.accessors.ButtonAccessor;
import com.danrus.pas.render.gui.ArmorStandNamerAdapter;
import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import com.mrbysco.armorposer.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ArmorPoserNamer implements ArmorStandNamerAdapter {

    private final ArmorStandScreen screen;

    public ArmorPoserNamer(ArmorStandScreen screen) {
        this.screen = screen;
    }

    @Override
    public void setNameInputValue(String value) {
        ( (ArmorStandScreenAccessor) screen).pas$getNameInput().setValue(value);
//        if (this.hasLevels() && !this.oldName.equals(this.changedName)) {
//            this.entityArmorStand.setCustomName(Component.literal(this.changedName));
//            Services.PLATFORM.renameArmorStand(this.entityArmorStand, this.changedName);
//            this.oldName = this.changedName;
//            this.updateRenameButton();
//        }
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
//?}
