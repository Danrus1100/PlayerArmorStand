package com.danrus.pas.impl.data;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.managers.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileTextureData implements DataHolder<SkinData> {

    private static final Map<String, SkinData> cache = new HashMap<>();
    public static final Path SKINS_PATH = VersioningUtils.getGameDir().resolve("pas/skins");

    @Override
    public SkinData get(NameInfo info) {
        if (cache.containsKey(info.base())) {
            return cache.get(info.base());
        }

        if (!info.params().contains("F")) {
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
                SkinManager.getInstance().getDataManager().store(info, new SkinData(info, skinLocation));
            });
        }
        SkinData data = new SkinData(info, skinLocation);
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
