package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.LegacySkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileTextureData implements DataProvider<LegacySkinData> {

    private static final Map<String, LegacySkinData> cache = new HashMap<>();
    public static final Path SKINS_PATH = VersioningUtils.getGameDir().resolve("pas/skins");

    @Override
    public LegacySkinData get(NameInfo info) {
        if (cache.containsKey(info.base())) {
            return cache.get(info.base());
        }

        if (!info.getDesiredProvider().equals("F")) {
            return null;
        }

        String encodedName = StringUtils.encodeToSha256(info.base());
        if (!isValidName(info.base())) {
            return null;
        }

        Path skinPath = SKINS_PATH.resolve(info.base() + ".png");
        ResourceLocation skinLocation = Rl.pas("random/" + encodedName);

        if (skinPath.toFile().exists()) {
            Minecraft.getInstance().execute(() -> {

                TextureUtils.registerTexture(skinPath, skinLocation, true);
                PasManager.getInstance().getDataManager().store(info, new LegacySkinData(info, skinLocation));
            });
        }
        LegacySkinData data = new LegacySkinData(info, skinLocation);
        data.setStatus(DownloadStatus.COMPLETED);
        cache.put(info.base(), data);
        return data;
    }

    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 16 && name.matches("[a-zA-Z0-9_]+");
    }

    @Override
    public boolean delete(NameInfo info) {
//        return SKINS_PATH.resolve(StringUtils.matchASName(string).get(0) + ".png").toFile().delete();
        return true;
    }

    @Override
    public HashMap<String, LegacySkinData> getAll() {
        // NO OP
        return new HashMap<>();
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof LegacySkinData;
    }

    @Override
    public String getName() {
        return "texture_file";
    }
}
