package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.VersioningUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.resources.Identifier;

public class ArmorStandCapeLayer extends VersioningUtils.VersionlessArmorStandCapeLayer {
    public ArmorStandCapeLayer(VersioningUtils.VersionlessArmorStandCape parent) {
        super(parent);
    }

    @Override
    public void draw(PoseStack poseStack, RenderVersionContext context, NameInfo info, int light, boolean isBaby)
    {

        if (!info.wantCape()) {
            return;
        }

        if (!(this.getParentModel() instanceof PlayerArmorStandModel model)) {
            return;
        }


        CapeData capeData = PasManager.getInstance().getCapeDataManager().findData(info);

        if (capeData == null) {
            capeData = PasManager.getInstance().getCapeDataManager().getData(info);

            if (capeData != null && capeData.getStatus() == DownloadStatus.IN_PROGRESS) {
                return;
            }
        }

        Identifier capeTexture = PasManager.getInstance().getCapeWithOverlayTexture(info);

        if (capeTexture == null) {
            return;
        }

        if (PlayerArmorStandModel.showArmorStandWhileDownload(capeData)) {
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
        poseStack.translate(0.0F, 0.02F, -0.12F);
        if (isBaby) {
        //? if <= 1.21.1 {
        
            /*poseStack.translate(0.0F, 0.71F, 0.21F);
            poseStack.scale(0.5F, 0.5F, 0.5F);
        *///?} else {
            poseStack.translate(0.0F, -0.02F, 0.21F);
        //?}
        }


        context.getCape().draw(poseStack, capeTexture, context, light);

        poseStack.popPose();

    }
}
