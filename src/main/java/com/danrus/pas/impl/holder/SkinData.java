package com.danrus.pas.impl.holder;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.ResourceLocation;

public class SkinData extends AbstractPasHolder{
    public static ResourceLocation DEFAULT_TEXTURE = getDefaultTextureStatic();

    public SkinData(NameInfo info) {
        super(info);
    }

    @Override
    protected ResourceLocation getDefaultTexture() {
        return getDefaultTextureStatic();
    }

    protected static ResourceLocation getDefaultTextureStatic() {
        return PasConfig.getInstance().isShowArmorStandWhileDownloading()
                ? Rl.vanilla("textures/entity/armorstand/wood.png")
                : Rl.vanilla("textures/entity/player/wide/steve.png");
    }

    @Override
    public void setTexture(ResourceLocation location) {
        this.location = location;
    }
}
