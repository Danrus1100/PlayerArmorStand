package com.danrus.pas.utils.info;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoParseException;
import com.danrus.pas.api.info.NameInfoParser;
import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.api.reg.FeatureRegistry;
import com.danrus.pas.impl.info.LegacyNameInfoParser;
import com.danrus.pas.impl.info.V2NameInfoParser;

import java.util.*;
import java.util.regex.Pattern;

public class NIParser {

    private final Map<String, NameInfo> cached = new WeakHashMap<>();
    private final List<String> failed = new ArrayList<>();
    private final List<NameInfoParser> parsers;

    public NIParser() {
        this.parsers = createParsers();
    }

    private List<NameInfoParser> createParsers() {
        List<NameInfoParser> list = new LinkedList<>();

        list.add(new LegacyNameInfoParser());
        list.add(new V2NameInfoParser());

        return list;
    }

    public NameInfo parse(String input) {
        if (failed.contains(input)) return NameInfo.EMPTY;
        if (cached.containsKey(input)) return cached.get(input);
        if (input == null || input.isEmpty()) return new NameInfo();

        for (NameInfoParser parser : parsers) {
            if (parser.isCompatibleWith(input))  {
                try {
                    return parser.parse(input, this::cache);
                } catch (NameInfoParseException parseException) {
                    PlayerArmorStandsClient.LOGGER.error("Failed to parse name {}", input, parseException);
                    failed.add(input);
                }
                return NameInfo.EMPTY;
            }
        }

        return NameInfo.EMPTY;
    }

    private static final NIParser instance = new NIParser();

    public static NIParser getInstance() {
        return instance;
    }

    private void cache(String key, NameInfo value) {
        cached.put(key, value);
    }
}
