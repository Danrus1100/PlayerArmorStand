package com.danrus.pas.utils.providers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.render.bb.BbPasModel;
import com.danrus.pas.render.bb.core.BbModelContainer;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.OverlayMessageManger;
import com.danrus.pas.utils.managers.SkinManger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class BbSkinProvider implements SkinProvider {

    public static Path MODELS_PATH = VersioningUtils.getGameDir().resolve("pas/");

    @Override
    public void load(String string) {
        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();

        if (params.contains("B")) {
            File modelFile = MODELS_PATH.resolve(name + "/model.bbmodel").toFile();
            if (modelFile.exists()) {
                SkinData data = SkinManger.getInstance().getSkinProviderManager().download(name + "|" + params.replace("B", ""));
                if (data != null) {
                    try {
                        data.setModel(new BbPasModel(BbModelContainer.parse(modelFile)));
                        SkinManger.getInstance().getDataManager().store(name, data);
                    } catch (Exception e) {
                        data.setModel(null);
                        PlayerArmorStandsClient.LOGGER.error("BbSkinProvider: Failed to parse model for " + name, e);
                        OverlayMessageManger.getInstance().showFailMessage(name);
                    }
                }
            }
        }
    }

    @Override
    public String getLiteral() {
        return "B";
    }
}
