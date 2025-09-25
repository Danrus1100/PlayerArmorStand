package com.danrus.pas.utils;

import com.danrus.pas.api.adapter.PasId;
import com.danrus.pas.impl.adapter.PasIdModImpl;
import net.minecraft.resources.ResourceLocation;

public class Id {
    public static PasId of(String nameSpace, String path) {
        return new PasIdModImpl(nameSpace, path);
    }

    public static PasId of(String id) {
        String[] parts = id.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid ID format, expected 'namespace:path'");
        }
        return new PasIdModImpl(parts[0], parts[1]);
    }

    public static PasId of(ResourceLocation location) {
        return new PasIdModImpl(location);
    }
}
