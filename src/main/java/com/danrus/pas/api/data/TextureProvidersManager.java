package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;

public interface TextureProvidersManager {
    void addProvider(TextureProvider provider);
    void addProvider(TextureProvider provider, int priority);
    void download(NameInfo info);

}
