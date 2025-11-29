package com.danrus.pas.render.armorstand;

//? >=1.21.9 {

import com.danrus.pas.PlayerArmorStandsClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.armorstand.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class PasCapeModel extends ArmorStandArmorModel implements Cape {

    public PasCapeModel() {
        this(PlayerArmorStandsClient.capeDef);
    }

    public PasCapeModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createCapeLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.clearChild("head");
        partdefinition1.clearChild("hat");
        PartDefinition partdefinition2 = partdefinition.clearChild("body");
        partdefinition.clearChild("left_arm");
        partdefinition.clearChild("right_arm");
        partdefinition.clearChild("left_leg");
        partdefinition.clearChild("right_leg");
        partdefinition2.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, CubeDeformation.NONE, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void draw(PoseStack stack, Identifier textureLocation, RenderVersionContext context, int light) {
        SubmitNodeStorage collector = context.getData(SubmitNodeStorage.class, "collector");
        PasCapeModel capeModel = context.getData(PasCapeModel.class, "capeModel");
        ArmorStandRenderState armorStand = context.getData(ArmorStandRenderState.class, "armorStand");
        collector.submitModel(capeModel, armorStand, stack, RenderTypes.entitySolid(textureLocation), light, OverlayTexture.NO_OVERLAY, armorStand.outlineColor, null);
    }
}
//?}
