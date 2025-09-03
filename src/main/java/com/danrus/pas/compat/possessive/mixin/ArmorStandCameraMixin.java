package com.danrus.pas.compat.possessive.mixin;

//? if possessive {
import com.danrus.pas.compat.possessive.PossessiveRenderHand;
import com.danrus.pas.render.PlayerArmorStandModel;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.SkinManger;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.just_s.camera.ArmorStandCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(ArmorStandCamera.class)
public class ArmorStandCameraMixin {
    @Shadow
    @Final
    private ArmorStand possessedArmorStand;

    // For Just_S: ты бы сэкономил бы мне кучу времени и нервов, если бы свой if-else где ты выбираешь модель руки арморстенда как здесь вынес бы в отдельный метод изначально
    @Unique
    private static PossessiveRenderHand resolveHand(ModelPart playerHand, PlayerModel playerModel, PlayerArmorStandModel armorStandModel, String name, String params) {
//        if (playerHand == playerModel.rightArm) {
//            return armorStandModel.rightArm;
//        } else {
//            return armorStandModel.leftArm;
//        }

        if (playerHand.equals(playerModel.rightArm)) {
            if (params.contains("S")) {
                return PossessiveRenderHand.RIGHT_SLIM;
            } else if (!name.isEmpty()) {
                return PossessiveRenderHand.RIGHT_WIDE;
            } else {
                return PossessiveRenderHand.RIGHT_ORIGINAL;
            }
        } else  {
            if (params.contains("S")) {
                return PossessiveRenderHand.LEFT_SLIM;
            } else if (!name.isEmpty()) {
                return PossessiveRenderHand.LEFT_WIDE;
            } else {
                return PossessiveRenderHand.LEFT_ORIGINAL;
            }
        }
    }

    private static List<ModelPart> getPartsForRender(PossessiveRenderHand hand, PlayerArmorStandModel armorStandModel) {
        return switch (hand) {
            case LEFT_ORIGINAL -> List.of(armorStandModel.originalLeftArm);
            case RIGHT_ORIGINAL -> List.of(armorStandModel.originalRightArm);
            case LEFT_WIDE -> List.of(armorStandModel.leftArm, armorStandModel.leftSleeve);
            case RIGHT_WIDE -> List.of(armorStandModel.rightArm, armorStandModel.rightSleeve);
            case LEFT_SLIM -> List.of(armorStandModel.leftSlimArm, armorStandModel.leftSlimSleeve);
            case RIGHT_SLIM -> List.of(armorStandModel.rightSlimArm, armorStandModel.rightSlimSleeve);
        };
    }

    /**
     * @author Danrus110_
     * @reason Rewrite hand rendering for {@link PlayerArmorStandModel}
     */
    @Overwrite
    //? if =1.20.1 {
    /*public void onRenderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2) {
    *///?} else if =1.21.1 {
    /*public void onRenderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2) {
    *///?} else {
    public void onRenderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, ResourceLocation resourceLocation, ModelPart modelPart, boolean bl){
    //?}
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        ArmorStandRenderer entityRenderer = (ArmorStandRenderer)entityRenderDispatcher.getRenderer(this.possessedArmorStand);
        PlayerRenderer playerRenderer = (PlayerRenderer)entityRenderDispatcher.getRenderer(Minecraft.getInstance().player);
        PlayerModel playerModel = playerRenderer.getModel();
        PlayerArmorStandModel armorStandModel = (PlayerArmorStandModel) entityRenderer.getModel();
        List<String> matches = possessedArmorStand.getCustomName() != null ?
                StringUtils.matchASName(possessedArmorStand.getCustomName().getString())
                : List.of("", "");

        PossessiveRenderHand resolvedHand = resolveHand(modelPart, playerModel, armorStandModel, matches.get(0), matches.get(1));

        float leftZRot = -0.1F;
        float rightZRot = 0.1F;

        armorStandModel.leftArm.zRot = leftZRot;
        armorStandModel.rightArm.zRot = rightZRot;
        armorStandModel.originalLeftArm.zRot = leftZRot;
        armorStandModel.originalRightArm.zRot = rightZRot;
        armorStandModel.leftSleeve.zRot = leftZRot;
        armorStandModel.rightSleeve.zRot = rightZRot;
        armorStandModel.leftSlimArm.zRot = leftZRot;
        armorStandModel.rightSlimArm.zRot = rightZRot;
        armorStandModel.leftSlimSleeve.zRot = leftZRot;
        armorStandModel.rightSlimSleeve.zRot = rightZRot;


        getPartsForRender(resolvedHand, armorStandModel).forEach(armorStandArm ->{
            armorStandArm.resetPose();
            armorStandArm.visible = true;

            armorStandArm.render(poseStack, multiBufferSource.getBuffer(RenderType.entityTranslucent(SkinManger.getInstance().getSkinTexture(Component.literal(matches.get(0))))), i, OverlayTexture.NO_OVERLAY);
        });


    }
}
//?}