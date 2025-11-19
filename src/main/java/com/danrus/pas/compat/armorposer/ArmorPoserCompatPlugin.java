package com.danrus.pas.compat.armorposer;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.VersioningUtils;

public class ArmorPoserCompatPlugin extends PasCompatPlugin {
    @Override
    public boolean shouldApply() {
        //? if armorposer {
        return VersioningUtils.isModLoaded("armorposer");
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    protected String getModName() {
        return "Armor Poser";
    }
}
