package com.danrus.pas.utils.info;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.api.reg.FeatureRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

public class NIParser {

    private final Map<String, NameInfo> cached = new WeakHashMap<>();
//    private final Map<NameInfo, String> cachedKeys = new WeakHashMap<>();

    public NameInfo parse(String input) {
        if (cached.containsKey(input)) return cached.get(input);
        if (input == null || input.isEmpty()) return new NameInfo();

        String[] divided = input.split("\\|", 2);
        String name = divided[0].trim();

        if (name.matches(".*[<>:\"/\\\\?*].*")) return new NameInfo();

        // Build default feature map
        Map<Class<? extends RenameFeature>, RenameFeature> featureMap = new LinkedHashMap<>();
        for (RenameFeature def : FeatureRegistry.getInstance().getOrderedDefaults()) {
            featureMap.put(def.getClass(), def);
        }

        String legacy = "";
        if (divided.length > 1) {
            String params = divided[1].trim();

            for (RenameFeature def : FeatureRegistry.getInstance().getOrderedDefaults()) {
                RenameFeature parsed = def.parseFrom(params);
                if (parsed != null) {
                    featureMap.put(parsed.getClass(), parsed);
                    String compiled = parsed.compile();
                    if (!compiled.isEmpty()) {
                        params = params.replace(compiled, "").trim();
                    }
                }
            }
        }

        NameInfo info = new NameInfo(name, featureMap, null);
        cache(input, info);
        return info;

    }

    private static final NIParser instance = new NIParser();

    public static NIParser getInstance() {
        return instance;
    }

    private void cache(String key, NameInfo value) {
        cached.put(key, value);
//        cachedKeys.put(value, key);
    }

//    public boolean hasCached(NameInfo info) {
//        return cached.containsValue(info);
//    }
//
//    public String getCachedKey(NameInfo info) {
//        return cachedKeys.get(info);
//    }
}
