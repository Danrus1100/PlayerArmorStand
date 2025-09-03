package com.danrus.pas.utils.data;

import com.danrus.pas.api.DataCache;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTextureCache implements DataCache<SkinData> {

    private static final Map<String, SkinData> cache = new HashMap<>();
    public static final Path SKINS_PATH = VersioningUtils.getGameDir().resolve("pas/skins");

    @Override
    public SkinData get(String string) {
        if (cache.containsKey(string)) {
            return cache.get(string);
        }

        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();

        if (!params.contains("F")) {
            return null;
        }

        String encodedName = StringUtils.encodeToSha256(name);
        if (!isValidName(name)) {
            return null;
        }

        Path skinPath = SKINS_PATH.resolve(name + ".png");
        ResourceLocation skinLocation = VersioningUtils.getResourceLocation("pas", "random/" + encodedName);

        if (skinPath.toFile().exists()) {
            Minecraft.getInstance().execute(() -> {

                TextureUtils.registerTexture(skinPath, skinLocation, true);
                SkinManger.getInstance().getDataManager().store(encodedName, new SkinData(name, skinLocation, SkinData.DEFAULT_CAPE, params));
            });
        }
        SkinData data = new SkinData(name, skinLocation, SkinData.DEFAULT_CAPE, params);
        data.setStatus(DownloadStatus.COMPLETED);
        cache.put(string, data);
        return data;
    }

    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 16 && name.matches("[a-zA-Z0-9_]+");
    }

    @Override
    public boolean delete(String string) {
//        return SKINS_PATH.resolve(StringUtils.matchASName(string).get(0) + ".png").toFile().delete();
        return true;
    }

    @Override
    public HashMap<String, SkinData> getAll() {
        // NO OP
        return new HashMap<>();
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof SkinData;
    }

    @Override
    public String getName() {
        return "texture_file";
    }
}
