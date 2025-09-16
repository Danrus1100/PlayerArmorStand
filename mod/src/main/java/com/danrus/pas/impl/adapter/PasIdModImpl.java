package com.danrus.pas.impl.adapter;

import com.danrus.pas.api.adapter.PasId;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.resources.ResourceLocation;

public class PasIdModImpl implements PasId {

    private final ResourceLocation location;

    public PasIdModImpl(ResourceLocation location){
        this.location = location;
    }

    public PasIdModImpl(String namespace, String path){
        this.location = VersioningUtils.getResourceLocation(namespace, path);
    }

    @Override
    public String getNamespace() {
        return location.getNamespace();
    }

    @Override
    public String getPath() {
        return location.getPath();
    }

    @Override
    public String getString() {
        return location.getNamespace() + ":" + location.getPath();
    }
}
