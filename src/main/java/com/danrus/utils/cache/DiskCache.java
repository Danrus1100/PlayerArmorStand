package com.danrus.utils.cache;

import com.danrus.PASModelData;
import com.danrus.interfaces.DataCache;
import com.danrus.utils.StringUtils;
import com.danrus.utils.TextureUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;

public class DiskCache implements DataCache<Path> {

    private Path CACHE_PATH = FabricLoader.getInstance().getGameDir().resolve("cache/pas");
    private HashMap<String, Identifier> identifiers = new HashMap<>();

    public DiskCache() {
        if (!CACHE_PATH.toFile().exists()) {
            CACHE_PATH.toFile().mkdirs();
        }
    }

    public DiskCache(Path cachePath) {
        this.CACHE_PATH = cachePath;
        if (!CACHE_PATH.toFile().exists()) {
            CACHE_PATH.toFile().mkdirs();
        }
    }

    public DiskCache(String path) {
        this.CACHE_PATH = Path.of(path);
        if (!CACHE_PATH.toFile().exists()) {
            CACHE_PATH.toFile().mkdirs();
        }
    }

    @Override
    public Path get(String string) {
        String name = StringUtils.matchASName(string).get(0);
        PASModelData data = new PASModelData(name);
        String encodedName = StringUtils.encodeToSha256(name);
        Path SkinFilePath = CACHE_PATH.resolve(encodedName + "png");
        if (SkinFilePath.toFile().exists()) {
            Identifier skinIdentifier = Identifier.of("pas", "skins/" + encodedName);
            TextureUtils.registerTexture(SkinFilePath, skinIdentifier);
            identifiers.put(name, skinIdentifier);
            data.setSkinTexture(skinIdentifier);
        }
        Path CapeFilePath = CACHE_PATH.resolve(encodedName + "_cape.png");
        if (CapeFilePath.toFile().exists()) {
            Identifier capeIdentifier = Identifier.of("pas", "capes/" + encodedName);
            TextureUtils.registerTexture(CapeFilePath, capeIdentifier);
            identifiers.put(name + "_cape", capeIdentifier);
            data.setCapeTexture(capeIdentifier);
        }
        return SkinFilePath;
    }

    @Override
    public void store(String name, Path data) {
        return; // No need to store data in disk cache, it is handled by TextureUtils
    }

    @Override
    public void invalidateData(String name) {
        return; // No need to invalidate data in disk cache
    }

    @Override
    public Identifier getSkinTexture(String name) {
        return identifiers.get(name);
    }

    @Override
    public Identifier getCapeTexture(String name) {
        return identifiers.get(name + "_cape");
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof Path || data instanceof String;
    }
}
