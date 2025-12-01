package com.danrus.pas.utils;

import net.minecraft.resources.ResourceLocation;

public class Rl {
    public static ResourceLocation of(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation pas(String path) {
        return ResourceLocation.fromNamespaceAndPath("pas", path);
    }

    public static ResourceLocation vanilla(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
