package com.danrus.pas.render.bb.core;

import com.danrus.pas.render.bb.core.adapter.BbOutlinerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BbModelLoader {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BbOutliner.class, new BbOutlinerAdapter())
            .create();

    public static BbModelContainer load(String json) {
        return GSON.fromJson(json, BbModelContainer.class);
    }
}
