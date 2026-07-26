package com.danrus.pas.impl.holder;

import com.danrus.pas.utils.mc.Id;
import net.minecraft.resources.Identifier;

public class CapeData extends AbstractPasHolder{
    public static Identifier DEFAULT_TEXTURE = Id.pas("capes/cape.png");

    public CapeData() {
        super();
    }

    @Override
    protected Identifier getDefaultTexture() {
        return Id.pas("capes/cape.png");
    }
}
