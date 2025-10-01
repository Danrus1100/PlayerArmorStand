package com.danrus.pas.api;

import java.util.function.Consumer;

public interface SkinProvider {
    void load(String name, Consumer<String> onComplete);
    String getLiteral();
}
