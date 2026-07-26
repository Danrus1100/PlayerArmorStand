package com.danrus.pas.api.info;

import java.util.function.BiConsumer;

public interface NameInfoParser {
    boolean isCompatibleWith(String input);
    NameInfo parse(String input, BiConsumer<String, NameInfo> cache);
}
