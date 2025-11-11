package com.danrus.pas.impl.holder;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.ResourceLocation;

public class CapeData extends AbstractPasHolder{
    public static ResourceLocation DEFAULT_TEXTURE = Rl.pas("capes/cape.png");

    private ResourceLocation location = DEFAULT_TEXTURE;

    public CapeData(NameInfo info) {
        super(info);
    }

    @Override
    public ResourceLocation getTexture(NameInfo info) {
        return location;
    }

    @Override
    public void setTexture(ResourceLocation location) {
        this.location = location;
    }
}
