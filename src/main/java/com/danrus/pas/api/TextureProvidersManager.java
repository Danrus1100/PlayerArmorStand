package com.danrus.pas.api;

public interface TextureProvidersManager {
    void addProvider(TextureProvider provider);
    void addProvider(TextureProvider provider, int priority);
    void download(NameInfo info);

}
