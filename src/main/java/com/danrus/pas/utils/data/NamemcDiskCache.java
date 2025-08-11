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

public class NamemcDiskCache implements DataCache<Path> {

    public static Path CACHE_PATH = VersioningUtils.getGameDir().resolve("cache/pas");
    private Path cachePath = CACHE_PATH;
    private HashMap<String, ResourceLocation> identifiers = new HashMap<>();

    @Override
    public SkinData get(String string) {
        String name = StringUtils.matchASName(string).get(0);
        SkinData data = new SkinData(name);
        Path SkinFilePath = cachePath.resolve(name + "_namemc.png");
        if (SkinFilePath.toFile().exists()) {
            ResourceLocation skinIdentifier = VersioningUtils.getResourceLocation("pas", "skins/" + name);
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

    @Override
    public String getName() {
        return "namemc";
    }
}