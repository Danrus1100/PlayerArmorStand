package com.danrus.pas.impl.holder;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;

public abstract class AbstractPasHolder implements DataHolder {

    private DownloadStatus status = DownloadStatus.NOT_STARTED;
    private final NameInfo info;

    public AbstractPasHolder(NameInfo info) {
        this.info = info;
    }

    @Override
    public DownloadStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    @Override
    public NameInfo getInfo() {
        return this.info;
    }
}
