package com.danrus.render;

import com.danrus.utils.StringUtils;
import com.danrus.utils.interfaces.ModelWithCape;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.MathHelper;

import java.util.List;

//TODO: figura mod support
//TODO: приклеить ноги к туловищу
//TODO: Возможность выбора модели по умолчанию (Игрок или Оригинал)
//TODO: Сделать отдельный FeatureRenderer для basePlate

//? if >=1.21.2 {
/*import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
*///?}

public class PlayerArmorStandModel extends ArmorStandArmorEntityModel implements ModelWithCape {
//    private final List<ModelPart> parts;
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

//        this.parts = root.traverse().filter(part -> !part.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation)  {
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, dilation, 1.0F, 0.5F), pivot(0.0F, 0.0F, 0.0F));

        modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), pivot(5.0F, 2.0F, 0.0F));
        modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(5.0F, 2.0F, 0.0F));
        modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(-5.0F, 2.0F, 0.0F));
        modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), pivot(1.9F, 12.0F, 0.0F));
        modelPartData.addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(1.9F, 12.0F, 0.0F));
        modelPartData.addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(-1.9F, 12.0F, 0.0F));
        modelPartData.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);

        modelPartData.addChild("right_body_stick", ModelPartBuilder.create().uv(16, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, dilation), pivot(-4.0F, 2.0F, 0.0F));
        modelPartData.addChild("left_body_stick", ModelPartBuilder.create().uv(48, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, dilation), pivot(4.0F, 2.0F, 0.0F));
        modelPartData.addChild("shoulder_stick", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, dilation), pivot(0.0F, 2.0F, 0.0F));
        modelPartData.addChild("base_plate", ModelPartBuilder.create().uv(0, 32).cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F, dilation), pivot(0.0F, 23.0F, 0.0F));

        // slim
        modelPartData.addChild("left_slim_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), pivot(5.0F, 2.5F, 0.0F));
        modelPartData.addChild("right_slim_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), pivot(-5.0F, 2.5F, 0.0F));
        modelPartData.addChild("left_slim_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(5.0F, 2.5F, 0.0F));
        modelPartData.addChild("right_slim_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), pivot(-5.0F, 2.5F, 0.0F));

        // original
        modelPartData.addChild("original_head", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F), pivot(0.0F, 1.0F, 0.0F));
        modelPartData.addChild("original_body", ModelPartBuilder.create().uv(0, 26).cuboid(-6.0F, 0.0F, -1.5F, 12.0F, 3.0F, 3.0F), ModelTransform.NONE);
        modelPartData.addChild("original_right_arm", ModelPartBuilder.create().uv(24, 0).cuboid(-2.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), pivot(-5.0F, 2.0F, 0.0F));
        modelPartData.addChild("original_left_arm", ModelPartBuilder.create().uv(32, 16).mirrored().cuboid(0.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), pivot(5.0F, 2.0F, 0.0F));
        modelPartData.addChild("original_right_leg", ModelPartBuilder.create().uv(8, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), pivot(-1.9F, 12.0F, 0.0F));
        modelPartData.addChild("original_left_leg", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), pivot(1.9F, 12.0F, 0.0F));
        modelPartData.addChild("right_body_stick", ModelPartBuilder.create().uv(16, 0).cuboid(-3.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), ModelTransform.NONE);
        modelPartData.addChild("left_body_stick", ModelPartBuilder.create().uv(48, 16).cuboid(1.0F, 3.0F, -1.0F, 2.0F, 7.0F, 2.0F), ModelTransform.NONE);
        modelPartData.addChild("shoulder_stick", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, 10.0F, -1.0F, 8.0F, 2.0F, 2.0F), ModelTransform.NONE);
        modelPartData.addChild("base_plate", ModelPartBuilder.create().uv(0, 32).cuboid(-6.0F, 11.0F, -6.0F, 12.0F, 1.0F, 12.0F), pivot(0.0F, 12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public ModelPart getCape(){
        return this.cloak;
    }

    private static ModelTransform pivot(float x, float y, float z) {
        //? <=1.21.4 {
        return ModelTransform.pivot(x, y, z);
         //?} else {
        /*return ModelTransform.origin(x, y, z);
        *///?}

    }

    private static float getPitch(EulerAngle angle){
        //? <=1.21.4 {
        return angle.getPitch();
         //?} else {
        /*return angle.pitch();
        *///?}
    }

    private static float getYaw(EulerAngle angle){
        //? <=1.21.4 {
        return angle.getYaw();
         //?} else {
        /*return angle.yaw();
        *///?}
    }

    private static float getRoll(EulerAngle angle){
        //? <=1.21.4 {
        return angle.getRoll();
         //?} else {
        /*return angle.roll();
        *///?}
    }

    //? if <1.21.2
    @Override
    public Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head, this.hat, this.originalHead);
    }

    //? if <1.21.2
    @Override
    public Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(
                this.body,
                this.rightArm,
                this.leftArm,
                this.rightLeg,
                this.leftLeg,
                this.jacket,
                this.rightSleeve,
                this.leftSleeve,
                this.rightPants,
                this.leftPants,
                this.leftSlimSleeve,
                this.rightSlimSleeve,
                this.leftSlimArm,
                this.rightSlimArm,
                this.originalBody,
                this.originalRightArm,
                this.originalLeftArm,
                this.originalRightLeg,
                this.originalLeftLeg,
                this.basePlate,
                this.leftBodyStick,
                this.rightBodyStick,
                this.shoulderStick
        );
    }

    //? if <1.21.2 {
    public void setAngles(ArmorStandEntity armorStand, float f, float g, float h, float i, float j){
        super.setAngles(armorStand, f, g, h, i, j);
        boolean showBase = !armorStand.shouldHideBasePlate();
        boolean showArms = armorStand.shouldShowArms();
        Text customName = armorStand.getCustomName();
        EulerAngle bodyRotation = armorStand.getBodyRotation();

    //?} else {
    /*@Override
    public void setAngles(ArmorStandEntityRenderState armorStand) {
        super.setAngles(armorStand);
        boolean showBase = armorStand.showBasePlate;
        boolean showArms = armorStand.showArms;
        Text customName = armorStand.customName;
        EulerAngle bodyRotation = armorStand.bodyRotation;
    *///?}

        this.leftPants.copyTransform(leftLeg);
        this.rightPants.copyTransform(rightLeg);

        this.leftSleeve.copyTransform(leftArm);
        this.rightSleeve.copyTransform(rightArm);

        this.leftSlimArm.copyTransform(leftArm);
        this.rightSlimArm.copyTransform(rightArm);
        this.leftSlimSleeve.copyTransform(leftArm);
        this.rightSlimSleeve.copyTransform(rightArm);

        this.originalBody.copyTransform(body);
        this.originalHead.copyTransform(head);
        this.originalRightArm.copyTransform(rightArm);
        this.originalLeftArm.copyTransform(leftArm);
        this.originalRightLeg.copyTransform(rightLeg);
        this.originalLeftLeg.copyTransform(leftLeg);

        this.jacket.copyTransform(body);

        //? <=1.21.1 {
        this.basePlate.yaw = ((float)Math.PI / 180F) * -armorStand.getYaw();
         //?} else {
        /*this.basePlate.yaw = ((float)Math.PI / 180F) * -armorStand.yaw;
        *///?}



        if (customName == null) {
            this.setModelVisibility(false, false, showBase);
            this.originalLeftArm.visible = showArms;
            this.originalRightArm.visible = showArms;
            this.rightBodyStick.pitch = ((float)Math.PI / 180F) * getPitch(bodyRotation);
            this.rightBodyStick.yaw = ((float)Math.PI / 180F) * getYaw(bodyRotation);
            this.rightBodyStick.roll = ((float)Math.PI / 180F) * getRoll(bodyRotation);
            this.leftBodyStick.pitch = ((float)Math.PI / 180F) * getPitch(bodyRotation);
            this.leftBodyStick.yaw = ((float)Math.PI / 180F) * getYaw(bodyRotation);
            this.leftBodyStick.roll = ((float)Math.PI / 180F) * getRoll(bodyRotation);
            this.shoulderStick.pitch = ((float)Math.PI / 180F) * getPitch(bodyRotation);
            this.shoulderStick.yaw = ((float)Math.PI / 180F) * getYaw(bodyRotation);
            this.shoulderStick.roll = ((float)Math.PI / 180F) * getRoll(bodyRotation);
            return;
        }
        if (customName != null){
            if (StringUtils.matchASName(customName.getString()).get(1).contains("S")){
                this.setModelVisibility(true, true, showBase);
            } else {
                this.setModelVisibility(true, false, showBase);
            }
        }


    }
    
    private void setModelVisibility(boolean player, boolean slim, boolean showBase) {
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

    public String getName() {
        return name;
    }

    public boolean isSlim() {
        return isSlim;
    }

    public boolean isOriginal() {
        return isOriginal;
    }
}
