package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;

import java.util.function.Consumer;

public interface TextureProvider {
    void load(NameInfo info, Consumer<String> onComplete);
    String getLiteral();
}
