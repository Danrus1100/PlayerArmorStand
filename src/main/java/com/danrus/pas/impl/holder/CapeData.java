package com.danrus.pas.impl.holder;

import com.danrus.pas.utils.Id;
import net.minecraft.resources.ResourceLocation;

public class CapeData extends AbstractPasHolder{
    public static ResourceLocation DEFAULT_TEXTURE = Id.pas("capes/cape.png");

    public CapeData() {
        super();
    }

    @Override
    protected ResourceLocation getDefaultTexture() {
        return Id.pas("capes/cape.png");
    }
}
