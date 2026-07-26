package com.danrus.pas.impl.info;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoParser;
import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.api.reg.FeatureRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class LegacyNameInfoParser implements NameInfoParser {

    @Override
    public boolean isCompatibleWith(String input) {
        return input != null && !input.contains("||");
    }

    @Override
    public NameInfo parse(String input, BiConsumer<String, NameInfo> cache) {
        String[] divided = input.split("\\|", 2);
        String name = divided[0].trim();

        if (name.matches(".*[<>:\"/\\\\?*].*")) return new NameInfo();

        Map<Class<? extends RenameFeature>, RenameFeature> featureMap = new LinkedHashMap<>();
        for (RenameFeature def : FeatureRegistry.getInstance().getOrderedDefaults()) {
            featureMap.put(def.getClass(), def);
        }

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

        NameInfo info = new NameInfo(name, featureMap, null, 1);
        cache.accept(input, info);
        return info;
    }
}
