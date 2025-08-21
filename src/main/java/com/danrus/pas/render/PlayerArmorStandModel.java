package com.danrus.pas.render;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.List;

public class PlayerArmorStandModel extends ArmorStandArmorModel implements ModelWithCape {
    // WIDE
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;

    // SLIM
    public final ModelPart leftSlimArm;
    public final ModelPart rightSlimArm;
    public final ModelPart leftSlimSleeve;
    public final ModelPart rightSlimSleeve;

    // ORIGINAL
    public final ModelPart rightBodyStick;
    public final ModelPart leftBodyStick;
    public final ModelPart shoulderStick;
    public final ModelPart basePlate;

    public final ModelPart originalHead;
    public final ModelPart originalBody;
    public final ModelPart originalRightArm;
    public final ModelPart originalLeftArm;
    public final ModelPart originalRightLeg;
    public final ModelPart originalLeftLeg;

    public final ModelPart jacket;
    private final ModelPart cloak;

    private String name;
    private boolean isSlim = false;
    private boolean isOriginal = false;

    public PlayerArmorStandModel(){
        this(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE).bakeRoot());
    }

    public PlayerArmorStandModel(ModelPart root) {
        super(root);
        this.cloak = root.getChild("cloak");
        this.leftSleeve = root.getChild("left_sleeve");
        this.rightSleeve = root.getChild("right_sleeve");
        this.leftPants = root.getChild("left_pants");
        this.rightPants = root.getChild("right_pants");
        this.jacket = root.getChild("jacket");

        // SLIM
        this.leftSlimArm = root.getChild("left_slim_arm");
        this.rightSlimArm = root.getChild("right_slim_arm");
        this.leftSlimSleeve = root.getChild("left_slim_sleeve");
        this.rightSlimSleeve = root.getChild("right_slim_sleeve");

        // ORIGINAL
        this.rightBodyStick = root.getChild("right_body_stick");
        this.leftBodyStick = root.getChild("left_body_stick");
        this.shoulderStick = root.getChild("shoulder_stick");
        this.basePlate = root.getChild("base_plate");

        this.originalHead = root.getChild("original_head");
        this.originalBody = root.getChild("original_body");
        this.originalRightArm = root.getChild("original_right_arm");
        this.originalLeftArm = root.getChild("original_left_arm");
        this.originalRightLeg = root.getChild("original_right_leg");
        this.originalLeftLeg = root.getChild("original_left_leg");

        this.hat.visible = true;

        this.leftSleeve.visible = true;
        this.rightSleeve.visible = true;
        this.leftPants.visible = true;
        this.rightPants.visible = true;
        this.jacket.visible = true;

        this.rightBodyStick.visible = false;
        this.leftBodyStick.visible = false;
        this.shoulderStick.visible = false;
        this.basePlate.visible = false;
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation){
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, deformation, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);

        partdefinition.addOrReplaceChild("right_body_stick", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, deformation), PartPose.offset(-4.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_body_stick", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, deformation), PartPose.offset(4.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("shoulder_stick", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, deformation), PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("base_plate", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F, deformation), PartPose.offset(0.0F, 23.0F, 0.0F));

        // slim
        partdefinition.addOrReplaceChild("left_slim_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_slim_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("left_slim_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_slim_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));

        // original
        partdefinition.addOrReplaceChild("original_head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offset(0.0F, 1.0F, 0.0F));
        partdefinition.addOrReplaceChild("original_body", CubeListBuilder.create().texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 3.0F, 3.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("original_right_arm", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("original_left_arm", CubeListBuilder.create().texOffs(32, 16).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("original_right_leg", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("original_left_leg", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_body_stick", CubeListBuilder.create().texOffs(16, 0).addBox(-3.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_body_stick", CubeListBuilder.create().texOffs(48, 16).addBox(1.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("shoulder_stick", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, 10.0F, -1.0F, 8.0F, 2.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("base_plate", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 11.0F, -6.0F, 12.0F, 1.0F, 12.0F), PartPose.offset(0.0F, 12.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(
            //? if <= 1.21.1 {
            /*ArmorStand
            *///?} else {
            net.minecraft.client.renderer.entity.state.ArmorStandRenderState
            //?}
                    armorStand
            //? if <= 1.21.1
            /*, float f, float g, float h, float i, float j*/
    ){
        super.setupAnim(armorStand
                //? if <= 1.21.1
                /*, f, g, h, i, j*/
        );
        boolean showBase = !VersioningUtils.getNoBasePlate(armorStand);
        boolean showArms = VersioningUtils.getIsShowArms(armorStand);
        Component customName = VersioningUtils.getCustomName(armorStand);
        Rotations bodyPose = VersioningUtils.getBodyPose(armorStand);

        //? if <= 1.21.1
        /*this.hat.copyFrom(this.head);*/

        this.leftPants.copyFrom(leftLeg);
        this.leftPants.copyFrom(leftLeg);
        this.rightPants.copyFrom(rightLeg);

        this.leftSleeve.copyFrom(leftArm);
        this.rightSleeve.copyFrom(rightArm);

        this.leftSlimArm.copyFrom(leftArm);
        this.rightSlimArm.copyFrom(rightArm);
        this.leftSlimSleeve.copyFrom(leftArm);
        this.rightSlimSleeve.copyFrom(rightArm);

        this.originalBody.copyFrom(body);
        this.originalHead.copyFrom(head);
        this.originalRightArm.copyFrom(rightArm);
        this.originalLeftArm.copyFrom(leftArm);
        this.originalRightLeg.copyFrom(rightLeg);
        this.originalLeftLeg.copyFrom(leftLeg);

        this.jacket.copyFrom(body);

        this.basePlate.yRot = ((float)Math.PI / 180F) * -VersioningUtils.getYRot(armorStand);

        if (!ModConfig.get().enableMod) {
            setOriginalAngles(showBase, showArms, bodyPose);
            return;
        }


        String customNameString;
        if (!ModConfig.get().defaultSkin.isEmpty() && customName == null) {
            customNameString = ModConfig.get().defaultSkin;
        } else if (customName != null) {
            customNameString = customName.getString();
        } else {
            customNameString = "";

        }

        List<String> matches =  StringUtils.matchASName(customNameString);
        boolean isDownlading = SkinManger.getInstance().getData(customName).getStatus() == DownloadStatus.IN_PROGRESS ||
                SkinManger.getInstance().getData(customName).getStatus() == DownloadStatus.FAILED;
        boolean showArmorStandWhileDownload = ModConfig.get().showArmorStandWhileDownloading && isDownlading;

        this.setModelVisibility(!showArmorStandWhileDownload, matches.get(1).contains("S"), showBase);

        if (customName == null && ModConfig.get().defaultSkin.isEmpty()) {
            setOriginalAngles(showBase, showArms, bodyPose);
        }

    }

    public Iterable<ModelPart> bodyParts()  {
        return List.of(
                this.hat, this.head, this.body, this.jacket,
                this.leftArm, this.rightArm, this.leftSleeve, this.rightSleeve,
                this.leftPants, this.rightPants, this.leftLeg, this.rightLeg,
                this.leftSlimArm, this.rightSlimArm, this.leftSlimSleeve, this.rightSlimSleeve,
                this.originalHead, this.originalBody, this.originalRightArm, this.originalLeftArm,
                this.originalRightLeg, this.originalLeftLeg,
                this.rightBodyStick, this.leftBodyStick, this.shoulderStick, this.basePlate
        );
    }

    public Iterable<ModelPart> headParts() {
        return List.of(this.hat, this.head, this.originalHead);
    }

    private void  setModelVisibility(boolean player, boolean slim, boolean showBase) {

        this.hat.visible = player;
        this.head.visible = player;
        this.body.visible = player;
        this.jacket.visible = player;
        this.leftArm.visible = player && !slim;
        this.rightArm.visible = player && !slim;
        this.leftSleeve.visible = player && !slim;
        this.rightSleeve.visible = player && !slim;
        this.leftPants.visible = player;
        this.rightPants.visible = player;
        this.leftLeg.visible = player;
        this.rightLeg.visible = player;

        this.leftSlimArm.visible = player && slim;
        this.rightSlimArm.visible = player && slim;
        this.leftSlimSleeve.visible = player && slim;
        this.rightSlimSleeve.visible = player && slim;

        this.originalHead.visible = !player;
        this.originalBody.visible = !player;
        this.originalRightArm.visible = !player;
        this.originalLeftArm.visible = !player;
        this.originalRightLeg.visible = !player;
        this.originalLeftLeg.visible = !player;

        this.rightBodyStick.visible = !player;
        this.leftBodyStick.visible = !player;
        this.shoulderStick.visible = !player;
        this.basePlate.visible = showBase && !player;

        this.cloak.visible = false;
    }

    private void setOriginalAngles(boolean showBase, boolean showArms, Rotations bodyPose) {
        this.setModelVisibility(false, false, showBase);
        this.originalLeftArm.visible = showArms;
        this.originalRightArm.visible = showArms;
        this.rightBodyStick.xRot = ((float)Math.PI / 180F) * VersioningUtils.getXRot(bodyPose);
        this.rightBodyStick.yRot = ((float)Math.PI / 180F) * VersioningUtils.getYRot(bodyPose);
        this.rightBodyStick.zRot = ((float)Math.PI / 180F) * VersioningUtils.getZRot(bodyPose);
        this.leftBodyStick.xRot = ((float)Math.PI / 180F) * VersioningUtils.getXRot(bodyPose);
        this.leftBodyStick.yRot = ((float)Math.PI / 180F) * VersioningUtils.getYRot(bodyPose);
        this.leftBodyStick.zRot = ((float)Math.PI / 180F) * VersioningUtils.getZRot(bodyPose);
        this.shoulderStick.xRot = ((float)Math.PI / 180F) * VersioningUtils.getXRot(bodyPose);
        this.shoulderStick.yRot = ((float)Math.PI / 180F) * VersioningUtils.getYRot(bodyPose);
        this.shoulderStick.zRot = ((float)Math.PI / 180F) * VersioningUtils.getZRot(bodyPose);
    }

    @Override
    public ModelPart getCape() {
        return this.cloak;
    }
}
