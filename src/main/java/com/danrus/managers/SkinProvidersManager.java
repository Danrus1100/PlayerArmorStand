package com.danrus.managers;

import com.danrus.PASClient;
import com.danrus.interfaces.AbstractSkinProvider;
import com.danrus.utils.StringUtils;
import com.danrus.utils.providers.FiguraSkinProvider;
import com.danrus.utils.providers.MojangSkinProvider;
import com.danrus.utils.providers.NamemcSkinProvider;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkinProvidersManager {

    private HashMap<String, AbstractSkinProvider> providers = new HashMap<>();
    private String exludeLiterals = "NF";

    private void addProvider(AbstractSkinProvider provider, String literal) {
        provider.setLiteral(literal);
        providers.put(literal, provider);
    }

    public SkinProvidersManager() {
        addProvider(new MojangSkinProvider(), "M");
        addProvider(new NamemcSkinProvider(), "N");
        addProvider(new FiguraSkinProvider(), "F");
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
