package com.danrus.interfaces;

import com.danrus.PASModelData;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public interface DataCache<T> {
    PASModelData get(String string);
    default HashMap<String, PASModelData> getAll() {
        return new HashMap<String, PASModelData>();
    }
    default void store(String name, T data){}
    default void invalidateData(String name){}
    boolean isCompatibleWith(Object data);
}
