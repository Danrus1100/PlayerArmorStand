package com.danrus.pas.impl.holder;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class AbstractPasHolder implements DataHolder {

    private DownloadStatus status = DownloadStatus.NOT_STARTED;
    protected ResourceLocation location;

    public AbstractPasHolder() {}

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
    public int hashCode() {
         return Objects.hash(this.getStatus());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SkinData other) {
            boolean statusEqual = this.getStatus() == other.getStatus();

            return statusEqual;
        }
        return false;
    }
}
