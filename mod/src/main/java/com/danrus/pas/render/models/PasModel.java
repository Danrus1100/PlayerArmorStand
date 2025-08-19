package com.danrus.pas.render.models;

import com.danrus.pas.render.ModelWithCape;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.List;

public class PasModel extends ArmorStandArmorModel implements ModelWithCape {

    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;
    private final ModelPart cloak;

    public PasModel(ModelPart modelPart) {
        super(modelPart);
        this.leftSleeve = modelPart.getChild("left_sleeve");
        this.rightSleeve = modelPart.getChild("right_sleeve");
        this.leftPants = modelPart.getChild("left_pants");
        this.rightPants = modelPart.getChild("right_pants");
        this.jacket = modelPart.getChild("jacket");
        this.cloak = modelPart.getChild("cloak");
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, float yOffset, boolean slim) {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, yOffset);
        PartDefinition partDefinition = meshDefinition.getRoot();

        if (slim) {
            PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
            PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.0F, 0.0F));
            partDefinition2.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
            partDefinition3.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        } else {
            PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
            PartDefinition partDefinition3 = partDefinition.getChild("right_arm");
            partDefinition2.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
            partDefinition3.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        }
        partDefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        partDefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, deformation, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));
        PartDefinition partDefinition3 = partDefinition.getChild("right_leg");
        partDefinition2.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        partDefinition3.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);

        return meshDefinition;
    }

    public static LayerDefinition createLayer(CubeDeformation deformation, boolean slim) {
        return LayerDefinition.create(PasModel.createMesh(deformation, 0.0f, slim), 64, 64);
    }

    public Iterable<ModelPart> headParts() {
        return List.of(this.hat, this.head);
    }

    public Iterable<ModelPart> bodyParts()  {
        return List.of(
                this.hat, this.head, this.body, this.jacket,
                this.leftArm, this.rightArm, this.leftSleeve, this.rightSleeve,
                this.leftPants, this.rightPants, this.leftLeg, this.rightLeg
        );
    }

    @Override
    public ModelPart getCape() {
        return this.cloak;
    }
}
