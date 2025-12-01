package com.danrus.pas.impl.namer;

//? if armorposer {

import com.danrus.pas.compat.armorposer.mixin.accessor.ArmorStandScreenAccessor;
import com.danrus.pas.render.gui.ArmorStandNamerAdapter;
import com.mrbysco.armorposer.client.gui.ArmorStandScreen;
import com.mrbysco.armorposer.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ArmorPoserNamer implements ArmorStandNamerAdapter {

    private final ArmorStandScreenAccessor accessor;
    private final ArmorStandScreen screen;

    public ArmorPoserNamer(ArmorStandScreen screen) {
        this.accessor = (ArmorStandScreenAccessor) screen;
        this.screen = screen;
    }

    @Override
    public void setNameInputValue(String value) {
        accessor.pas$setChangedName(value);
        if (accessor.pas$invokeHasLevels() && !accessor.pas$getOldName().equals(accessor.pas$getChangedName())) {
            accessor.pas$getEntityArmorStand().setCustomName(Component.literal(accessor.pas$getChangedName()));
            Services.PLATFORM.renameArmorStand(accessor.pas$getEntityArmorStand(), accessor.pas$getChangedName());
            accessor.pas$setOldName(accessor.pas$getChangedName());
            accessor.pas$invokeUpdateRenameButton();
            accessor.pas$getNameInput().setValue(value);
        }
    }

    @Override
    public String getNameInputValue() {
        return accessor.pas$getOldName();
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
}
//?}
