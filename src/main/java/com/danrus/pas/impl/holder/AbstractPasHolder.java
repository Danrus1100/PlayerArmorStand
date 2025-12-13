package com.danrus.pas.impl.holder;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class AbstractPasHolder implements DataHolder {

    private DownloadStatus status = DownloadStatus.NOT_STARTED;
    private final NameInfo info;
    protected ResourceLocation location;

    public AbstractPasHolder(NameInfo info) {
        this.info = info;
    }

    @Override
    public ResourceLocation getTexture(NameInfo info) {
        if (location != null) return location;
        return getDefaultTexture();
    }

    protected abstract ResourceLocation getDefaultTexture();

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

    @Override
    public int hashCode() {
         return Objects.hash(this.getInfo(), this.getStatus());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SkinData other) {
            boolean infoEqual = this.getInfo().equals(other.getInfo());
            boolean statusEqual = this.getStatus() == other.getStatus();

            return infoEqual && statusEqual;
        }
        return false;
    }
}
