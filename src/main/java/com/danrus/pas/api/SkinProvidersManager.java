package com.danrus.pas.api;

public interface SkinProvidersManager {
    void addProvider(SkinProvider provider);
    SkinData download(String string);
}
