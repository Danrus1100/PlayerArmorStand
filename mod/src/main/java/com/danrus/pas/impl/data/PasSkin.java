package com.danrus.pas.impl.data;


import com.danrus.pas.render.models.PasModel;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

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

    private ResourceLocation skin;
    private  EntityModel<?> model;

    public PasSkin(Path skin, String name, boolean slim) {
        this.model = new PasModel(PasModel.createLayer(CubeDeformation.NONE, slim).bakeRoot());
        this.skin = VersioningUtils.getResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
        // Register texture asynchronously
        CompletableFuture<ResourceLocation> future = TextureUtils.registerTexture(skin, VersioningUtils.getResourceLocation("pas", "skins/" + StringUtils.encodeToSha256(name)), true);
        future.thenAccept(resourceLocation -> this.skin = resourceLocation);
    }

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
