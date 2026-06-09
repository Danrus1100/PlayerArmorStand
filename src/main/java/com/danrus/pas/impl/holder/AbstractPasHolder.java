package com.danrus.pas.impl.holder;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractPasHolder implements DataHolder {

    private volatile DownloadStatus status = DownloadStatus.NOT_STARTED;
    protected volatile ResourceLocation location;

    public AbstractPasHolder() {}

    @Override
    public ResourceLocation getTexture() {
        if (location != null) return location;
        return getDefaultTexture();
    }

    @Override
    public void setTexture(ResourceLocation location) {
        this.location = location;
    }

    protected abstract ResourceLocation getDefaultTexture();

    @Override
    public DownloadStatus getStatus() {
        return status;
    }

    @Override
    public synchronized void setStatus(DownloadStatus status) {
        this.status = status;
    }

    @Override
    public synchronized boolean compareAndSetStatus(DownloadStatus expected, DownloadStatus update) {
        if (this.status == expected) {
            this.status = update;
            return true;
        }
        return false;
    }
}
