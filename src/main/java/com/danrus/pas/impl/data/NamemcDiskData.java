package com.danrus.pas.impl.data;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.api.DataHolder;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.HashMap;

public class NamemcDiskData implements DataHolder<Path> {

    public static Path CACHE_PATH = VersioningUtils.getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, ResourceLocation> cache = new HashMap<>();

    @Override
    public SkinData get(NameInfo info) {
        SkinData data = new SkinData(info);
        Path SkinFilePath = cachePath.resolve(info.base() + "_namemc.png");
        if (SkinFilePath.toFile().exists()) {
            ResourceLocation skinIdentifier = Rl.pas( "skins/" + info.base());
            TextureUtils.registerTexture(SkinFilePath, skinIdentifier, true);
            cache.put(info.base(), skinIdentifier);
            data.setSkinTexture(skinIdentifier);
            PasManager.getInstance().getDataManager().store(info, data);
            return data;
        }
        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        Path skinFilePath = cachePath.resolve(info.base() + "_namemc.png");
        if (skinFilePath.toFile().exists()) {
            cache.remove(info.base());
            return skinFilePath.toFile().delete();
        }
        return false;
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof Path;
    }

    @Override
    public String getName() {
        return "namemc";
    }
}