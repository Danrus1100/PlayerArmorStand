package com.danrus.utils.providers;

import com.danrus.PASClient;
import com.danrus.SkinManger;
import com.danrus.enums.DownloadStatus;
import com.danrus.interfaces.SkinProvider;
import com.danrus.utils.StringUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkinProvidersManager {

    private HashMap<String, SkinProvider> providers = new HashMap<>();
    private String exludeLiterals = "NF";

    private void addProvider(SkinProvider provider, String literal) {
        provider.setLiteral(literal);
        providers.put(literal, provider);
    }

    public SkinProvidersManager() {
        addProvider(new MojangSkinProvider(), "M");
        addProvider(new NamemcSkinProvider(), "N");
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
            PASClient.LOGGER.warn("SkinProvidersManager: No provider found for " + name + " with params: " + params);
            SkinManger.getInstance().getDataManager().invalidateData(name);
        }
    }

}
