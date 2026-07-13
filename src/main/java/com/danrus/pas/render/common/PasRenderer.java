package com.danrus.pas.render.common;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.Id;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PasRenderer {

    public static ResourceLocation WOOD = Id.vanilla(
            //? <26.1
            "textures/entity/armorstand/wood.png"
            //? >=26.1
            //"textures/entity/armorstand/armorstand.png"
    );
    public static ResourceLocation STEVE = Id.vanilla("textures/entity/player/wide/steve.png");

    private final PlayerArmorStandModel model;

    public PasRenderer(PlayerArmorStandModel model) {
        this.model = model;
    }

    public void draw(SkinData skinData, @Nullable CapeData capeData, NameInfo info, PasRenderContext context, PasModelSettings settings, PoseStack poseStack, int packedLight, int packedOverlay) {
        setupAnim(model, settings);
        for (ModelPart part : this.model.getOriginalParts()) {
            renderPart(poseStack, part, RenderType.entityCutout(WOOD), context, packedLight, packedOverlay);
        }
        boolean showDefaultSkin = info.isEmpty() || PlayerArmorStandModel.showArmorStandWhileDownload(Optional.of(skinData));
        ResourceLocation location = showDefaultSkin ? STEVE : PasManager.getInstance().getSkinWithOverlayTexture(info);
        for (ModelPart part : this.model.getPlayerParts()) {
            renderPart(poseStack, part, RenderType.entityTranslucent(location), context, packedLight, packedOverlay);
        }

        if (settings.foil()) {
            for (ModelPart part : this.model.getOriginalParts()) {
                renderPart(poseStack, part, RenderType.glint(), context, packedLight, packedOverlay);
            }

            for (ModelPart part : this.model.getPlayerParts()) {
                renderPart(poseStack, part, RenderType.glint(), context, packedLight, packedOverlay);
            }
        }

        if (capeData != null && !capeData.getTexture().equals(CapeData.DEFAULT_TEXTURE)) {
            renderPart(poseStack, model.getCape(), RenderType.entityTranslucent(capeData.getTexture()), context, packedLight, packedOverlay);

            if (settings.foil()) {
                renderPart(poseStack, model.getCape(), RenderType.glint(), context, packedLight, packedOverlay);
            }
        }
    }

    private static void setupAnim(PlayerArmorStandModel model, PasModelSettings settings) {
        model.setupAnim(settings.poseSettings().toRenderState());
    }

    private static void renderPart(PoseStack poseStack, ModelPart part,
                                   //? if <1.21.11 {
                                   RenderType
                                           //?} else {
                                           /*net.minecraft.client.renderer.rendertype.RenderType
                                            *///?}
                                           type, PasRenderContext context, int packedLight, int packedOverlay) {
        //? if <1.21.9 {
        MultiBufferSource bufferSource = context.getData(MultiBufferSource.class,"bufferSource");
        VertexConsumer skinConsumer = bufferSource.getBuffer(type);
        part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        //?} else {
        /*net.minecraft.client.renderer.SubmitNodeCollector nodeCollector = context.getData(net.minecraft.client.renderer.SubmitNodeCollector.class,"collector");
        nodeCollector.submitModelPart(part, poseStack, type, packedLight, packedOverlay, null);
        *///?}
    }
}
