package com.danrus.pas.impl.holder;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.Identifier;

public class SkinData extends AbstractPasHolder{
    public static Identifier DEFAULT_TEXTURE = PasConfig.getInstance().isShowArmorStandWhileDownloading()
            ? Rl.vanilla("textures/entity/armorstand/wood.png")
            : Rl.vanilla("textures/entity/player/wide/steve.png");

    private Identifier location = DEFAULT_TEXTURE;

    public SkinData(NameInfo info) {
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
