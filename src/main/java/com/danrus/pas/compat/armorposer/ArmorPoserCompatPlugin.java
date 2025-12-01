package com.danrus.pas.compat.armorposer;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.ModUtils;

public class ArmorPoserCompatPlugin extends PasCompatPlugin {
    @Override
    public boolean shouldApply() {
        //? if armorposer {
        return ModUtils.isModLoaded("armorposer");
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    protected String getModName() {
        return "Armor Poser";
    }
}
