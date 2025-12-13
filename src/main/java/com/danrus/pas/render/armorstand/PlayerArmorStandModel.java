package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.item.ArmorStandSpecialRenderer;
import com.danrus.pas.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.ArmorStandArmorModel;
//? if < 1.21.9 {
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
//?}
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PlayerArmorStandModel extends ArmorStandArmorModel implements Cape, Cloneable {
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
    private final ModelPart leftEar;
    private final ModelPart rightEar;

    private boolean isSlim = false;
    private boolean isOriginal = false;

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

        //DeadMou5
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");

        this.hat.visible = true;
        this.leftEar.visible = false;
        this.rightEar.visible = false;

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

        // DeadMou5
        PartDefinition headDefinition = partdefinition.getChild("head");
        CubeListBuilder earsCubeListBuilder = CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F));
        headDefinition.addOrReplaceChild("left_ear", earsCubeListBuilder, PartPose.offset(-6.0F, -6.0F, 0.0F));
        headDefinition.addOrReplaceChild("right_ear", earsCubeListBuilder, PartPose.offset(6.0F, -6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ArmorStandRenderState armorStand){
        this.setupAnim(armorStand, true);

    }

    public void setupAnim(ArmorStandRenderState armorStand, boolean setupVisibility) {
        super.setupAnim(armorStand);
        boolean showBase = !ModUtils.getNoBasePlate(armorStand);
        boolean showArms = ModUtils.getIsShowArms(armorStand);
        Component customName = ModUtils.getCustomName(armorStand);
        Rotations bodyPose = ModUtils.getBodyPose(armorStand);
        NameInfo info = NameInfo.parse(customName);

        cpp(leftLeg, this.leftPants);
        cpp(rightLeg, this.rightPants);

        cpp(leftArm, this.leftSleeve);
        cpp(rightArm, this.rightSleeve);

        cpp(leftArm, this.leftSlimArm);
        cpp(rightArm, this.rightSlimArm);
        cpp(leftArm, this.leftSlimSleeve);
        cpp(rightArm, this.rightSlimSleeve);

        cpp(body, this.originalBody);
        cpp(head, this.originalHead);
        cpp(rightArm, this.originalRightArm);
        cpp(leftArm, this.originalLeftArm);
        cpp(rightLeg, this.originalRightLeg);
        cpp(leftLeg, this.originalLeftLeg);

        cpp(body, this.jacket);


        this.basePlate.yRot = ((float)Math.PI / 180F) * -ModUtils.getYRot(armorStand);

        if (!PasConfig.getInstance().isEnableMod()) {
            setOriginalAngles(showBase, showArms, bodyPose);
            return;
        }


        String customNameString;
        if (!PasConfig.getInstance().getDefaultSkin().isEmpty() && customName == null) {
            customNameString = PasConfig.getInstance().getDefaultSkin();
        } else if (customName != null) {
            customNameString = customName.getString();
        } else {
            customNameString = "";
        }

        boolean isEarsVisible = "deadmau5".equalsIgnoreCase(info.base()) && PasConfig.getInstance().isShowEasterEggs();
        this.leftEar.visible = isEarsVisible;
        this.rightEar.visible = isEarsVisible;

        if (setupVisibility) {
            this.setModelVisibility(!showArmorStandWhileDownload(PasManager.getInstance().findSkinData(info)), info.wantBeSlim(), showBase);
        }

        if (customNameString.isEmpty() && PasConfig.getInstance().getDefaultSkin().isEmpty()) {
            setOriginalAngles(showBase, showArms, bodyPose);
        }
    }

    public void setupForItem(ArmorStandSpecialRenderer.ArmorStandItemState state, NameInfo info) {

        this.hat.visible = state.head.mode.showPlayerPart(info);
        this.head.visible = state.head.mode.showPlayerPart(info);
        this.body.visible = state.body.mode.showPlayerPart(info);
        this.jacket.visible = state.body.mode.showPlayerPart(info);
        this.leftArm.visible = state.leftArm.mode.showPlayerPart(info) && !info.wantBeSlim();
        this.rightArm.visible = state.rightArm.mode.showPlayerPart(info) && !info.wantBeSlim();
        this.leftSleeve.visible = state.leftArm.mode.showPlayerPart(info) && !info.wantBeSlim();
        this.rightSleeve.visible = state.rightArm.mode.showPlayerPart(info) && !info.wantBeSlim();
        this.leftPants.visible = state.leftLeg.mode.showPlayerPart(info);
        this.rightPants.visible = state.rightLeg.mode.showPlayerPart(info);
        this.leftLeg.visible = state.leftLeg.mode.showPlayerPart(info);
        this.rightLeg.visible = state.rightLeg.mode.showPlayerPart(info);
        this.leftSlimArm.visible = state.leftArm.mode.showPlayerPart(info) && info.wantBeSlim();
        this.rightSlimArm.visible = state.rightArm.mode.showPlayerPart(info) && info.wantBeSlim();
        this.leftSlimSleeve.visible = state.leftArm.mode.showPlayerPart(info) && info.wantBeSlim();
        this.rightSlimSleeve.visible = state.rightArm.mode.showPlayerPart(info) && info.wantBeSlim();
        this.originalHead.visible = state.head.mode.showOriginalPart(info);
        this.originalBody.visible = state.body.mode.showOriginalPart(info);
        this.originalRightArm.visible = state.rightArm.mode.showOriginalPart(info);
        this.originalLeftArm.visible = state.leftArm.mode.showOriginalPart(info);
        this.originalRightLeg.visible = state.rightLeg.mode.showOriginalPart(info);
        this.originalLeftLeg.visible = state.leftLeg.mode.showOriginalPart(info);
        this.rightBodyStick.visible = state.body.mode.showOriginalPart(info);
        this.leftBodyStick.visible = state.body.mode.showOriginalPart(info);
        this.shoulderStick.visible = state.body.mode.showOriginalPart(info);

        this.basePlate.visible = state.baseplate;
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
        return List.of(this.hat, this.head, this.originalHead, this.leftEar, this.rightEar);
    }

    public Iterable<ModelPart> getPlayerHeadParts() {
        return List.of(this.hat, this.head);
    }

    public Iterable<ModelPart> getOriginalHead() {
        return List.of(this.originalHead);
    }

    public Iterable<ModelPart> getBasePartsForItem() {
        return List.of(
                this.rightBodyStick,
                this.leftBodyStick,
                this.shoulderStick,
                this.basePlate,
                this.originalBody,
                this.originalLeftLeg,
                this.originalRightLeg
        );
    }

    public Iterable<ModelPart> getOriginalParts() {
        return List.of(
                this.originalBody,
                this.originalLeftArm,
                this.originalRightArm,
                this.originalLeftLeg,
                this.originalRightLeg,
                this.rightBodyStick,
                this.leftBodyStick,
                this.shoulderStick,
                this.basePlate,
                this.originalHead
        );
    }

    public Iterable<ModelPart> getPlayerParts() {
        return List.of(
                this.body,
                this.jacket,
                this.leftArm,
                this.rightArm,
                this.leftSleeve,
                this.rightSleeve,
                this.leftPants,
                this.rightPants,
                this.leftLeg,
                this.rightLeg,
                this.leftSlimArm,
                this.rightSlimArm,
                this.leftSlimSleeve,
                this.rightSlimSleeve,
                this.hat,
                this.head
        );
    }

    public void setModelVisibility(boolean player, boolean slim, boolean showBase) {

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
        this.rightBodyStick.xRot = ((float)Math.PI / 180F) * ModUtils.getXRot(bodyPose);
        this.rightBodyStick.yRot = ((float)Math.PI / 180F) * ModUtils.getYRot(bodyPose);
        this.rightBodyStick.zRot = ((float)Math.PI / 180F) * ModUtils.getZRot(bodyPose);
        this.leftBodyStick.xRot = ((float)Math.PI / 180F) * ModUtils.getXRot(bodyPose);
        this.leftBodyStick.yRot = ((float)Math.PI / 180F) * ModUtils.getYRot(bodyPose);
        this.leftBodyStick.zRot = ((float)Math.PI / 180F) * ModUtils.getZRot(bodyPose);
        this.shoulderStick.xRot = ((float)Math.PI / 180F) * ModUtils.getXRot(bodyPose);
        this.shoulderStick.yRot = ((float)Math.PI / 180F) * ModUtils.getYRot(bodyPose);
        this.shoulderStick.zRot = ((float)Math.PI / 180F) * ModUtils.getZRot(bodyPose);
    }

    public ModelPart getCape() {
        return this.cloak;
    }


    public static boolean showArmorStandWhileDownload(DataHolder data) {

        if (data == null) {
            return true;
        }

        boolean isDownlading = data.getStatus() == DownloadStatus.IN_PROGRESS ||
                data.getStatus() == DownloadStatus.FAILED || data.getStatus() == DownloadStatus.NOT_STARTED;
        return PasConfig.getInstance().isShowArmorStandWhileDownloading()&& isDownlading;
    }

    private static void cpp(ModelPart from, ModelPart to) {
        ModUtils.copyPartPose(from, to);
    }

    @Override
    public void draw(PoseStack stack, ResourceLocation textureLocation, RenderVersionContext context, int i) {
        //? <1.21.9 {
        getCape().visible = true;
        com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer = context.getData(MultiBufferSource.class, "multiBufferSource").getBuffer(RenderType.entitySolid(textureLocation));
        getCape().render(stack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
        //?}
    }

    @Override
    public PlayerArmorStandModel clone() {
        try {
            PlayerArmorStandModel clone = (PlayerArmorStandModel) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
