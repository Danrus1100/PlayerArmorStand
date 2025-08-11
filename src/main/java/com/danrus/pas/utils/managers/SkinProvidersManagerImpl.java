package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.api.SkinProvidersManager;
import com.danrus.pas.utils.StringUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkinProvidersManagerImpl implements SkinProvidersManager {

    private HashMap<String, SkinProvider> providers = new HashMap<>();
    private String exludeLiterals = "NF";

    public void addProvider(SkinProvider provider) {
        providers.put(provider.getLiteral(), provider);
    }


    public void download(String string) {
        String name = StringUtils.matchASName(string).get(0);
        String params = StringUtils.matchASName(string).get(1);
        AtomicBoolean needDownload = new AtomicBoolean(true);
        providers.forEach((literal, provider) -> {
            if (needDownload.get() && (params.contains(provider.getLiteral()) || (!params.matches(".*[" + exludeLiterals + "].*") && literal.equals("M")))) {
                needDownload.set(false);
                provider.load(name);
            }
        });
        if (needDownload.get()) {
            PlayerArmorStandsClient.LOGGER.warn(this.getClass().getName() + ": No provider found for " + name + " with params: " + params);
            SkinManger.getInstance().getDataManager().invalidateData(name);
        }
    }
}

