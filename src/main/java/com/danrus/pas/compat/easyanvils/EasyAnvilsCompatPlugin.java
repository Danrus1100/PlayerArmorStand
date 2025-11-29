package com.danrus.pas.compat.easyanvils;

import com.danrus.pas.compat.PasCompatPlugin;
import com.danrus.pas.utils.VersioningUtils;

public class EasyAnvilsCompatPlugin extends PasCompatPlugin {
    @Override
    protected boolean shouldApply() {
        //? if easyanvils {
        /*return VersioningUtils.isModLoaded("easyanvils");
        *///?} else {
        return false;
        //?}
    }

    @Override
    protected String getModName() {
        return "Easy Anvils";
    }
}
