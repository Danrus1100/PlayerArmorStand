package com.danrus.pas.render.layer;

import com.danrus.pas.render.bb.BbPasModel;
import com.danrus.pas.utils.VersioningUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BbPasLayer extends VersioningUtils.VersionlessArmorStandRenderLayer{

    public BbPasLayer(RenderLayerParent<ArmorStandRenderState, ArmorStandArmorModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ArmorStandRenderState renderState, float yRot, float xRot) {
        if (this.getParentModel() instanceof BbPasModel model) {
            model.getContainer().getElements().forEach(element -> {
                ModelPart part = model.getBaseParts().stream()
                        .filter(pendingPart -> pendingPart.getChild(element.name).visible)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Part not found: " + element.name));
                part.render(poseStack, bufferSource.getBuffer(
                        RenderType.itemEntityTranslucentCull(VersioningUtils.getGuiLocation("minecraft", "textures/entity/armor_stand.png"))),
                        packedLight, OverlayTexture.NO_OVERLAY
                );
            });
        }
    }
}
