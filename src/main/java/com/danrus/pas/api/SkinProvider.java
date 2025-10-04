package com.danrus.pas.api;

import java.util.function.Consumer;

public interface SkinProvider {
    void load(NameInfo info, Consumer<String> onComplete);
    String getLiteral();
}
