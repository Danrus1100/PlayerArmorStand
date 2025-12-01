package com.danrus.pas.config;

//? if modmenu {
import com.danrus.pas.render.gui.DummyConfigScreen;
import com.danrus.pas.utils.ModUtils;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //? if yacl {
        if (ModUtils.isModLoaded("yet_another_config_lib_v3")) {
            return YaclConfig::getConfigScreen;
        }
        //?}
        return DummyConfigScreen::new;
    }
}
//?}

