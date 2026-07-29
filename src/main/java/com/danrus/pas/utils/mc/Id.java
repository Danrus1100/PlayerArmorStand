package com.danrus.pas.utils.mc;

import com.danrus.pas.PlayerArmorStandsClient;
import net.minecraft.resources.Identifier;

public class Id {
    public static Identifier of(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier pas(String path) {
        return Identifier.fromNamespaceAndPath(PlayerArmorStandsClient.MOD_ID, path);
    }

    public static Identifier vanilla(String path) {
        return Identifier.withDefaultNamespace(path);
    }
}
