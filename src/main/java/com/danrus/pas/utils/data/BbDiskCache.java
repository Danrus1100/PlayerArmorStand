package com.danrus.pas.utils.data;

import com.danrus.pas.api.DataCache;
import com.danrus.pas.api.SkinData;

import java.util.HashMap;
import java.util.Map;

public class BbDiskCache implements DataCache<SkinData> {

    Map<String, SkinData> cache = new HashMap<>();

    @Override
    public SkinData get(String string) {
        if (cache.containsKey(string)) {
            return cache.get(string);
        }
        return null;
    }

    @Override
    public boolean delete(String string) {
        return cache.remove(string) != null;
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof SkinData;
    }

    @Override
    public String getName() {
        return "blockbench";
    }
}
