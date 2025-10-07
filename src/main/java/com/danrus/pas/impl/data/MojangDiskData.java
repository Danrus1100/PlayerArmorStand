package com.danrus.pas.impl.data;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DataHolder;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;

public class MojangDiskData implements DataHolder<Path> {

    public static final Path CACHE_PATH = VersioningUtils.getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, ResourceLocation> cache = new HashMap<>();

    public MojangDiskData() {
        if (!cachePath.toFile().exists()) {
            cachePath.toFile().mkdirs();
        }
    }

    @Override
    public SkinData get(NameInfo info) {
        SkinData data = new SkinData(info);
        String encodedName = StringUtils.encodeToSha256(info.base());
        boolean doStoreData = false;
        Path SkinFilePath = cachePath.resolve(encodedName + ".png");
        if (SkinFilePath.toFile().exists()) {
            ResourceLocation skin = Rl.pas("skins/" + encodedName);
            TextureUtils.registerTexture(SkinFilePath, skin, true);
            cache.put(info.base(), skin);
            data.setSkinTexture(skin);
            doStoreData = true;
            Path CapeFilePath = cachePath.resolve(encodedName + "_cape.png");
            if (CapeFilePath.toFile().exists()) {
                ResourceLocation cape = Rl.pas( "capes/" + encodedName);
                TextureUtils.registerTexture(CapeFilePath, cape, false);
                cache.put(info.base() + "_cape", cape);
                data.setCapeTexture(cape);
            }
        }
        if (doStoreData) {
            PasManager.getInstance().getDataManager().store(info, data);
            return data;
        }
        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        String encodedName = StringUtils.encodeToSha256(info.base());
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
            cache.remove(info.base());
            cache.remove(info.base() + "_cape");
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
