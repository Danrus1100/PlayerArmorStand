package com.danrus.pas.render;

import com.danrus.pas.utils.VersioningUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class ArmorStandCapeLayer extends VersioningUtils.VersionlessArmorStandCapeLayer {
    public ArmorStandCapeLayer(VersioningUtils.VersionlessArmorStandCape parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
                        //? if <= 1.21.1 {
                        /*ArmorStand
                        *///?} else {
                        net.minecraft.client.renderer.entity.state.ArmorStandRenderState
                        //?}
                        armorStand,
                        float f1, float f2
                        //? if <= 1.21.1
                        /*, float f3, float f4, float f5, float f6*/
    ) {
//        if (!ModConfig.get().enableMod || VersioningUtils.isInvisible(armorStand)) {
//            return;
//        }
//
//        var customName = VersioningUtils.getCustomName(armorStand);
//        boolean isBaby = VersioningUtils.getIsBaby(armorStand);
//
//        if (customName == null) {
//            return;
//        }
//
//        List<String> matches = StringUtils.matchASName(customName.getString());
//
//        boolean isDownlading = SkinManger.getInstance().getData(customName).getStatus() == DownloadStatus.IN_PROGRESS ||
//                SkinManger.getInstance().getData(customName).getStatus() == DownloadStatus.FAILED;
//        boolean showArmorStandWhileDownload = ModConfig.get().showArmorStandWhileDownloading && isDownlading;
//
//        if (matches.get(1).contains("C") && this.getParentModel() instanceof ModelWithCape model && !showArmorStandWhileDownload) {
//            SkinData skinData = SkinManger.getInstance().getData(customName);
//            ResourceLocation capeTexture = skinData.getCapeTexture();
//
//            poseStack.pushPose();
//            poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));
//            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
//            poseStack.translate(0.0F, 0.02F, -0.12F);
//            //? if <= 1.21.1 {
//            /*if (isBaby) {
//                poseStack.translate(0.0F, 0.71F, 0.21F);
//                poseStack.scale(0.5F, 0.5F, 0.5F);
//            }
//            *///?} else {
//            if (isBaby) {
//                poseStack.translate(0.0F, -0.02F, 0.21F);
////                poseStack.scale(0.5F, 0.5F, 0.5F);
//            }
//            //?}
//
//
//            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entitySolid(capeTexture));
//            model.getCape().visible = true;
//            model.getCape().render(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
//
//            poseStack.popPose();
        }
//    }
}
