package com.danrus.pas.impl.holder;

import com.danrus.pas.config.PasConfig;
import com.danrus.pas.utils.mc.Id;
import net.minecraft.resources.Identifier;

public class SkinData extends AbstractPasHolder{
    public static Identifier DEFAULT_TEXTURE = getDefaultTextureStatic();

    public SkinData() {
        super();
    }

    @Override
    protected Identifier getDefaultTexture() {
        return getDefaultTextureStatic();
    }

    public static Identifier getDefaultTextureStatic() {
        return PasConfig.getInstance().isShowArmorStandWhileDownloading()
                ? Id.vanilla(
                        //? <26.1
                        //"textures/entity/armorstand/wood.png"
                        //? >=26.1
                        "textures/entity/armorstand/armorstand.png"
                )
                : Id.vanilla("textures/entity/player/wide/steve.png");
    }
}
