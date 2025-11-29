package com.danrus.pas.utils;

import net.minecraft.resources.Identifier;

public class Rl {
    public static Identifier of(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier pas(String path) {
        return Identifier.fromNamespaceAndPath("pas", path);
    }

    public static Identifier vanilla(String path) {
        return Identifier.withDefaultNamespace(path);
    }
}
