package com.danrus.pas.impl.info;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoParseException;
import com.danrus.pas.api.info.NameInfoParser;
import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.api.reg.FeatureRegistry;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class V2NameInfoParser implements NameInfoParser {
    private static final String VERSION_SEPARATOR = "||";
    private static final String TOKEN_SEPARATOR = ";";

    @Override
    public boolean isCompatibleWith(String input) {
        return input != null && input.contains(VERSION_SEPARATOR);
    }

    @Override
    public NameInfo parse(String input, BiConsumer<String, NameInfo> cache) {
        String[] divided = input.split("\\|\\|", 2);
        String name = divided[0].trim();

        if (name.matches(".*[<>:\"/\\\\?*].*")) {
            return new NameInfo();
        }

        Map<Class<? extends RenameFeature>, RenameFeature> featureMap = new LinkedHashMap<>();
        for (RenameFeature feature : FeatureRegistry.getInstance().getOrderedDefaults()) {
            featureMap.put(feature.getClass(), feature);
        }

        String params = divided.length > 1 ? divided[1].trim() : "";
        if (params.isEmpty()) {
            throw unexpectedToken("");
        }

        Set<Class<? extends RenameFeature>> parsedFeatureTypes = new HashSet<>();
        for (String rawToken : params.split(TOKEN_SEPARATOR, -1)) {
            String token = rawToken.trim();
            RenameFeature parsed = parseToken(token);
            if (parsed == null || !parsedFeatureTypes.add(parsed.getClass())) {
                throw unexpectedToken(token);
            }
            featureMap.put(parsed.getClass(), parsed);
        }

        NameInfo info = new NameInfo(name, featureMap, null, 2);
        cache.accept(input, info);
        return info;
    }

    private RenameFeature parseToken(String token) {
        for (RenameFeature feature : FeatureRegistry.getInstance().getOrderedDefaults()) {
            try {
                RenameFeature parsed = feature.parseToken(token);
                if (parsed != null) {
                    return parsed;
                }
            } catch (RuntimeException exception) {
                throw unexpectedToken(token);
            }
        }
        return null;
    }

    private NameInfoParseException unexpectedToken(String token) {
        return new NameInfoParseException("Unexpected token '" + token + "'");
    }
}
