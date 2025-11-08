package com.danrus.pas.compat.possessive.mixin;

//? if possessive {
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.compat.possessive.PossessiveRenderHand;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.Rl;
import com.mojang.blaze3d.vertex.PoseStack;
import net.just_s.camera.ArmorStandCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    private static PossessiveRenderHand resolveHand(ModelPart playerHand, PlayerModel playerModel, PlayerArmorStandModel armorStandModel, NameInfo info) {
//        if (playerHand == playerModel.rightArm) {
//            return armorStandModel.rightArm;
//        } else {
//            return armorStandModel.leftArm;
//        }

        if (playerHand.equals(playerModel.rightArm)) {
            if (ModConfig.get().possessiveShowDefaultHand) {
                return PossessiveRenderHand.RIGHT_ORIGINAL;
            }

            if (info.wantBeSlim()) {
                return PossessiveRenderHand.RIGHT_SLIM;
            } else if (!info.isEmpty()) {
                return PossessiveRenderHand.RIGHT_WIDE;
            } else {
                return PossessiveRenderHand.RIGHT_ORIGINAL;
            }
        } else  {
            if (ModConfig.get().possessiveShowDefaultHand) {
                return PossessiveRenderHand.LEFT_ORIGINAL;
            }

            if (info.wantBeSlim()) {
                return PossessiveRenderHand.LEFT_SLIM;
            } else if (!info.isEmpty()) {
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
        NameInfo info = possessedArmorStand.getCustomName() != null ?
                NameInfo.parse(possessedArmorStand.getCustomName().getString())
                : new NameInfo();

        PossessiveRenderHand resolvedHand = resolveHand(modelPart, playerModel, armorStandModel, info);

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

        ResourceLocation skinTexture;
        if (!info.isEmpty()) {
            if (ModConfig.get().possessiveShowDefaultHand) {
                skinTexture = Rl.vanilla("textures/entity/armorstand/wood.png");
            } else {
                skinTexture = PasManager.getInstance().getSkinWithOverlayTexture(info);
            }
        } else {
            skinTexture = SkinData.DEFAULT_TEXTURE;
        }

        getPartsForRender(resolvedHand, armorStandModel).forEach(armorStandArm ->{
            armorStandArm.resetPose();
            armorStandArm.visible = true;

            armorStandArm.render(poseStack, multiBufferSource.getBuffer(RenderType.entityTranslucent(skinTexture)), i, OverlayTexture.NO_OVERLAY);
        });


    }
}
//?}