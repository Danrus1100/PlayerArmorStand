package com.danrus.pas.impl.holder;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.ResourceLocation;

public class CapeData extends AbstractPasHolder{
    public static ResourceLocation DEFAULT_TEXTURE = Rl.pas("capes/cape.png");

    private ResourceLocation location = DEFAULT_TEXTURE;

    @Override
    public ResourceLocation getResourceLocation(NameInfo info) {
        return location;
    }

    @Override
    public void setResourceLocation(ResourceLocation location) {
        this.location = location;
    }
}
