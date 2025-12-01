package com.danrus.pas.compat.possessive;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.ModUtils;

public class PossessiveCompatPlugin extends PasCompatPlugin {
    @Override
    public boolean shouldApply() {
        //? if possessive {
        return ModUtils.isModLoaded("possessive");
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    protected String getModName() {
        return "Possessive";
    }
}
