package com.danrus.pas.render.armorstand;

//? >=1.21.9 {

import com.danrus.pas.PlayerArmorStandsClient;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PasCapeModel extends ArmorStandArmorModel {

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
}
//?}
