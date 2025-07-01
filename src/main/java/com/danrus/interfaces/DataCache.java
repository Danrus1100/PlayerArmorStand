package com.danrus.interfaces;

import net.minecraft.util.Identifier;

public interface DataCache<T> {
    T get(String name);
    void store(String name, T data);
    void invalidateData(String name);
    Identifier getSkinTexture(String name);
    Identifier getCapeTexture(String name);
    boolean isCompatibleWith(Object data);
}
