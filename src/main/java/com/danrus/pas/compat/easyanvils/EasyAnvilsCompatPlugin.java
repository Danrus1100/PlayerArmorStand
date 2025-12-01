package com.danrus.pas.compat.easyanvils;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.ModUtils;

public class EasyAnvilsCompatPlugin extends PasCompatPlugin {
    @Override
    protected boolean shouldApply() {
        //? if easyanvils {
        return ModUtils.isModLoaded("easyanvils");
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    protected String getModName() {
        return "Easy Anvils";
    }
}
