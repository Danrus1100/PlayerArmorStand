package com.danrus.pas.impl.data;


import com.danrus.pas.render.models.PasModel;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;

public class PasSkin {

    public static PasSkin ARMOR_STAND = new PasSkin(
            VersioningUtils.getResourceLocation("minecraft", "textures/entity/armor_stand/wood.png"),
            new ArmorStandModel(ArmorStandModel.createBodyLayer().bakeRoot())
    );

    public static PasSkin STEVE = new PasSkin(
            VersioningUtils.getResourceLocation("minecraft", "textures/entity/player/wide/steve.png"),
            new PasModel(PasModel.createLayer(CubeDeformation.NONE, false).bakeRoot())
    );

    public static PasSkin ALEX = new PasSkin(
            VersioningUtils.getResourceLocation("minecraft", "textures/entity/player/slim/alex.png"),
            new PasModel(PasModel.createLayer(CubeDeformation.NONE, true).bakeRoot())
    );

    private final ResourceLocation skin;
    private final EntityModel<?> model;

    public PasSkin(ResourceLocation skin, EntityModel<?> model) {
        this.skin = skin;
        this.model = model;
    }

    public ResourceLocation getSkin() {
        return skin;
    }

    public EntityModel<?> getModel() {
        return model;
    }
}
