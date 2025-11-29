package com.danrus.pas.impl.holder;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.Identifier;

public class CapeData extends AbstractPasHolder{
    public static Identifier DEFAULT_TEXTURE = Rl.pas("capes/cape.png");

    private Identifier location = DEFAULT_TEXTURE;

    public CapeData(NameInfo info) {
        super(info);
    }

    @Override
    public Identifier getTexture(NameInfo info) {
        return location;
    }

    @Override
    public void setTexture(Identifier location) {
        this.location = location;
    }
}
