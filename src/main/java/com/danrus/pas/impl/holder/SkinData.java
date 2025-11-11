package com.danrus.pas.impl.holder;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.Rl;
import net.minecraft.resources.ResourceLocation;

public class SkinData extends AbstractPasHolder{
    public static ResourceLocation DEFAULT_TEXTURE = ModConfig.get().showArmorStandWhileDownloading
            ? Rl.vanilla("textures/entity/armorstand/wood.png")
            : Rl.vanilla("textures/entity/player/wide/steve.png");

    private ResourceLocation location = DEFAULT_TEXTURE;

    public SkinData(NameInfo info) {
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
