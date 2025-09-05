package com.danrus.pas.utils;

import java.nio.file.Path;

public class FileSystemHelper {

    private static final Path CACHED_SKIN_PATH = VersioningUtils.getGameDir().resolve("cache/pas");
    private static final Path RANDOM_SKIN_PATH = VersioningUtils.getGameDir().resolve("pas/skins");

    public static Path getCachedSkinPath(String string) {
        return CACHED_SKIN_PATH.resolve(StringUtils.encodeToSha256(StringUtils.getBaseName(string)) + ".png");
    }

    public static Path getRandomSkinPath(String string) {
        return RANDOM_SKIN_PATH.resolve(StringUtils.getBaseName(string) + ".png");
    }

}
