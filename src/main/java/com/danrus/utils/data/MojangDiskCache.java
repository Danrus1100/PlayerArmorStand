package com.danrus.utils.data;

import com.danrus.PASModelData;
import com.danrus.managers.SkinManger;
import com.danrus.interfaces.DataCache;
import com.danrus.utils.StringUtils;
import com.danrus.utils.TextureUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;

public class MojangDiskCache implements DataCache<Path> {

    public static Path CACHE_PATH = FabricLoader.getInstance().getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, Identifier> identifiers = new HashMap<>();

    public MojangDiskCache() {
        if (!cachePath.toFile().exists()) {
            cachePath.toFile().mkdirs();
        }
    }

    @Override
    public PASModelData get(String string) {
        String name = StringUtils.matchASName(string).get(0);
        PASModelData data = new PASModelData(name);
        String encodedName = StringUtils.encodeToSha256(name);
        boolean doStoreData = false;
        Path SkinFilePath = cachePath.resolve(encodedName + ".png");
        if (SkinFilePath.toFile().exists()) {
            Identifier skinIdentifier = Identifier.of("pas", "skins/" + encodedName);
            TextureUtils.registerTexture(SkinFilePath, skinIdentifier, true);
            identifiers.put(name, skinIdentifier);
            data.setSkinTexture(skinIdentifier);
            doStoreData = true;
        }
        Path CapeFilePath = cachePath.resolve(encodedName + "_cape.png");
        if (CapeFilePath.toFile().exists()) {
            Identifier capeIdentifier = Identifier.of("pas", "capes/" + encodedName);
            TextureUtils.registerTexture(CapeFilePath, capeIdentifier, false);
            identifiers.put(name + "_cape", capeIdentifier);
            data.setCapeTexture(capeIdentifier);
            doStoreData = true;
        }
        if (doStoreData) {
            SkinManger.getInstance().getDataManager().store(name, data);
            return data;
        }
        return null;
    }

    @Override
    public void drop(String name) {
        MinecraftClient.getInstance().getTextureManager().destroyTexture(identifiers.get(name));
        cachePath.resolve(StringUtils.encodeToSha256(name)).toFile().delete();
        identifiers.remove(name);
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof Path || data instanceof String;
    }
}
