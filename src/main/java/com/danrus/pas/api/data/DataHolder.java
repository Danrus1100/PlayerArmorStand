package com.danrus.pas.api.data;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import net.minecraft.resources.ResourceLocation;

public interface DataHolder {
    ResourceLocation getTexture(NameInfo info);
    DownloadStatus getStatus();
    void setStatus(DownloadStatus status);
    void setTexture(ResourceLocation location);
}
