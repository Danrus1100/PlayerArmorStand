package com.danrus.utils.cache;

import com.danrus.PASModelData;
import com.danrus.interfaces.DataCache;
import net.minecraft.util.Identifier;

import java.util.List;

public class DataManager<T> {
    private List<DataCache> sources;

    public void addSource(DataCache source) {
        sources.add(source);
    }

    public Identifier getSkin(String name) {
        for (DataCache source : sources) {
            Identifier data = source.getSkinTexture(name);
            if (data != null) {
                return data;
            }
        }
        // PASSkinDownloader.downloadAndRegisterSkin(name);
        return PASModelData.DEFAULT_TEXTURE;
    }

    public void store(String name, T data) {
        for (DataCache<?> source : sources) {
            if (source.isCompatibleWith(data)) {
                ((DataCache<T>) source).store(name, data);
            }
        }
    }

    public void invalidateData(String name) {
        for (DataCache source : sources) {
            source.invalidateData(name);
        }
    }
}
