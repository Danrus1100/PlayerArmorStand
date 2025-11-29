package com.danrus.pas.api.data;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import net.minecraft.resources.Identifier;

public interface DataHolder {
    Identifier getTexture(NameInfo info);
    DownloadStatus getStatus();
    void setStatus(DownloadStatus status);
    void setTexture(Identifier location);
    NameInfo getInfo();
}
