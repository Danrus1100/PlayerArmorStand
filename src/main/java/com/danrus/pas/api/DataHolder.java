package com.danrus.pas.api;

import net.minecraft.resources.ResourceLocation;

public interface DataHolder {
    ResourceLocation getTexture(NameInfo info);
    DownloadStatus getStatus();
    void setStatus(DownloadStatus status);
    void setTexture(ResourceLocation location);
    NameInfo getInfo();
}
