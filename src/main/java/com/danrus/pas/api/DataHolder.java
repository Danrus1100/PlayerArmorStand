package com.danrus.pas.api;

import net.minecraft.resources.ResourceLocation;

public interface DataHolder {
    ResourceLocation getResourceLocation(NameInfo info);
    DownloadStatus getStatus();
    void setStatus(DownloadStatus status);
    void setResourceLocation(ResourceLocation location);
}
