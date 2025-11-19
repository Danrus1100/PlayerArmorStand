package com.danrus.pas.compat.possessive;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.VersioningUtils;

public class PossessiveCompatPlugin extends PasCompatPlugin {
    @Override
    public boolean shouldApply() {
        //? if possessive {
        return VersioningUtils.isModLoaded("possessive");
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    protected String getModName() {
        return "Possessive";
    }
}
