package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.VersioningUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ArmorStandCapeLayer extends VersioningUtils.VersionlessArmorStandCapeLayer {

    //? >= 1.21.9
    /*private final PasCapeModel capeModel;*/

    public ArmorStandCapeLayer(VersioningUtils.VersionlessArmorStandCape parent) {
        super(parent);
        //? >= 1.21.9
        /*capeModel = new PasCapeModel();*/
    }

    //? <1.21.9 {
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
    )
    //?} else {
    /*@Override
    public void submit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector collector, int i, net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStand, float f, float g)
    *///?}
    {
        if (!ModConfig.get().enableMod || VersioningUtils.isInvisible(armorStand)) {
            return;
        }

        var customName = VersioningUtils.getCustomName(armorStand);
        boolean isBaby = VersioningUtils.getIsBaby(armorStand);

        if (customName == null) {
            return;
        }

        NameInfo info = NameInfo.parse(VersioningUtils.getCustomName(armorStand));
        CapeData skinData = PasManager.getInstance().getCapeDataManager().getData(info);

        if (info.wantCape() && this.getParentModel() instanceof PlayerArmorStandModel model) {
//            CapeData skinData = PasManager.getInstance().get
            if (PlayerArmorStandModel.showArmorStandWhileDownload(customName, skinData)) {
                return;
            }
            ResourceLocation capeTexture = PasManager.getInstance().getCapeTexture(info);

            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));
            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
            poseStack.translate(0.0F, 0.02F, -0.12F);
            //? if <= 1.21.1 {
            /*if (isBaby) {
                poseStack.translate(0.0F, 0.71F, 0.21F);
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }
            *///?} else {
            if (isBaby) {
                poseStack.translate(0.0F, -0.02F, 0.21F);
//                poseStack.scale(0.5F, 0.5F, 0.5F);
            }
            //?}


            model.getCape().visible = true;
            //? <1.21.9 {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entitySolid(capeTexture));
            model.getCape().render(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
            //?} else {
            /*collector.submitModel(capeModel, armorStand, poseStack, RenderType.entitySolid(capeTexture), i, OverlayTexture.NO_OVERLAY, armorStand.outlineColor, (net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay)null);
            *///?}

            poseStack.popPose();
        }
    }
}
