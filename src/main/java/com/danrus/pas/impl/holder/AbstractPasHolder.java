package com.danrus.pas.impl.holder;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DownloadStatus;

public abstract class AbstractPasHolder implements DataHolder {

    private DownloadStatus status = DownloadStatus.NOT_STARTED;

    @Override
    public DownloadStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DownloadStatus status) {
        this.status = status;
    }
}
