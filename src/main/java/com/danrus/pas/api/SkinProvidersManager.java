package com.danrus.pas.api;

public interface SkinProvidersManager {
    void addProvider(SkinProvider provider);
    void addProvider(SkinProvider provider, int priority);
    void download(String string);
}
