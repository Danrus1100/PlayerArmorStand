package com.danrus.utils.cache;

import com.danrus.PASModelData;
import com.danrus.interfaces.DataCache;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class GameCache implements DataCache<PASModelData> {

    private HashMap<String, PASModelData> cache = new HashMap<>();

    @Override
    public PASModelData get(String name) {
        return cache.get(name);
    }

    @Override
    public void store(String name, PASModelData data) {
        cache.put(name, data);
    }

    @Override
    public void invalidateData(String name) {
        if (cache.containsKey(name)) {
            cache.remove(name);
        }
    }

    @Override
    public Identifier getSkinTexture(String name) {
        return get(name).getSkinTexture();
    }

    @Override
    public Identifier getCapeTexture(String name) {
        return get(name).getCapeTexture();
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof PASModelData;
    }
}
