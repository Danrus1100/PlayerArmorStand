package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.NameInfo;

import java.util.HashMap;


// У меня идея: если MojangTextureProvider скачает текстуры, то он складывает их в cache, и тогда для плаща не надо будет делать отдельный запрос
// TODO: implement
public abstract class AbstractMojangDataProvider<T extends DataHolder> implements DataProvider<T> {

    private HashMap<String, T> dataMap = new HashMap<>();
    private HashMap<String, DownloadedData> cache = new HashMap<>();

    @Override
    public T get(NameInfo info) {
        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        return false;
    }

    @Override
    public HashMap<String, T> getAll() {
        return null;
    }

    @Override
    public void store(NameInfo info, Object data) {

    }

    @Override
    public void invalidateData(NameInfo info) {

    }

    @Override
    public String getName() {
        return "mojang";
    }

    public static class DownloadedData {
        public String skinUrl;
        public String capeUrl;
    }
}
