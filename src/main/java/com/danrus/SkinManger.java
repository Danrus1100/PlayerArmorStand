package com.danrus;

import com.danrus.utils.cache.DataManager;
import com.danrus.utils.cache.DiskCache;
import com.danrus.utils.cache.GameCache;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkinManger {

    private static final SkinManger INSTANCE = new SkinManger();

    private DataManager dataCache = new DataManager();

    public Identifier getSkinTexture(Text name) {
        if (name == null || name.getString().isEmpty()) {
            return PASModelData.DEFAULT_TEXTURE;
        }
        return dataCache.get(name.getString()).getSkinTexture();
    }

    public void init() {
        dataCache.addSource(new GameCache());
        dataCache.addSource(new DiskCache());
    }

    public static SkinManger getInstance() {
        return INSTANCE;
    }
}
