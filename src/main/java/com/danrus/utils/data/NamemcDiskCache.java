package com.danrus.utils.data;

import com.danrus.PASModelData;
import com.danrus.SkinManger;
import com.danrus.interfaces.DataCache;
import com.danrus.utils.StringUtils;
import com.danrus.utils.TextureUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;

public class NamemcDiskCache implements DataCache<Path> {

    public static Path CACHE_PATH = FabricLoader.getInstance().getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, Identifier> identifiers = new HashMap<>();

    @Override
    public PASModelData get(String string) {
        String name = StringUtils.matchASName(string).get(0);
        PASModelData data = new PASModelData(name);
        Path SkinFilePath = cachePath.resolve(name + "_namemc.png");
        if (SkinFilePath.toFile().exists()) {
            Identifier skinIdentifier = Identifier.of("pas", "skins/" + name);
            TextureUtils.registerTexture(SkinFilePath, skinIdentifier, true);
            identifiers.put(name, skinIdentifier);
            data.setSkinTexture(skinIdentifier);
            SkinManger.getInstance().getDataManager().store(name, data);
            return data;
        }
        return null;
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof Path;
    }
}
