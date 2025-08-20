package com.danrus.pas.utils.data;

import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DataCache;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;

public class MojangDiskCache implements DataCache<Path> {

    public static final Path CACHE_PATH = VersioningUtils.getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, ResourceLocation> identifiers = new HashMap<>();

    public MojangDiskCache() {
        if (!cachePath.toFile().exists()) {
            cachePath.toFile().mkdirs();
        }
    }

    @Override
    public SkinData get(String string) {
        String name = StringUtils.matchASName(string).get(0);
        SkinData data = new SkinData(name);
        String encodedName = StringUtils.encodeToSha256(name);
        boolean doStoreData = false;
        Path SkinFilePath = cachePath.resolve(encodedName + ".png");
        if (SkinFilePath.toFile().exists()) {
            ResourceLocation skin = VersioningUtils.getResourceLocation("pas", "skins/" + encodedName);
            TextureUtils.registerTexture(SkinFilePath, skin, true);
            identifiers.put(name, skin);
            data.setSkinTexture(skin);
            doStoreData = true;
        }
        Path CapeFilePath = cachePath.resolve(encodedName + "_cape.png");
        if (CapeFilePath.toFile().exists()) {
            ResourceLocation cape = VersioningUtils.getResourceLocation("pas", "capes/" + encodedName);
            TextureUtils.registerTexture(CapeFilePath, cape, false);
            identifiers.put(name + "_cape", cape);
            data.setCapeTexture(cape);
            doStoreData = true;
        }
        if (doStoreData) {
            SkinManger.getInstance().getDataManager().store(name, data);
            return data;
        }
        return null;
    }

    @Override
    public boolean delete(String string) {
        String name = StringUtils.matchASName(string).get(0);
        String encodedName = StringUtils.encodeToSha256(name);
        Path skinFilePath = cachePath.resolve(encodedName + ".png");
        Path capeFilePath = cachePath.resolve(encodedName + "_cape.png");
        boolean deleted = false;
        if (skinFilePath.toFile().exists()) {
            deleted = skinFilePath.toFile().delete();
        }
        if (capeFilePath.toFile().exists()) {
            deleted = capeFilePath.toFile().delete();
        }
        if (deleted) {
            identifiers.remove(name);
            identifiers.remove(name + "_cape");
        }
        return deleted;
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof Path || data instanceof String;
    }

    @Override
    public String getName() {
        return "mojang";
    }
}
