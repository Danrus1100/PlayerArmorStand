package com.danrus.render;

import com.danrus.utils.PASModelData;
import com.danrus.utils.StringUtils;
import com.danrus.utils.interfaces.ModelWithCape;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
//?}

//? if >=1.21.2 {
public class ASCapeFeatureRenderer extends FeatureRenderer<ArmorStandEntityRenderState, ArmorStandArmorEntityModel> {
    public ASCapeFeatureRenderer(FeatureRendererContext<ArmorStandEntityRenderState, ArmorStandArmorEntityModel> context) {
//?} else {
/*public class ASCapeFeatureRenderer extends FeatureRenderer<ArmorStandEntity , ArmorStandArmorEntityModel> {
    public ASCapeFeatureRenderer(FeatureRendererContext<ArmorStandEntity, ArmorStandArmorEntityModel> context) {
*///?}
        super(context);
    }

    //? if >=1.21.2 {
    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, ArmorStandEntityRenderState state, float limbAngle, float limbDistance) {
        if (!state.invisible && state.customName != null) {
            if (this.getContextModel() instanceof ModelWithCape model) {
                var customName = state.customName;
                boolean isBaby = state.baby;


    //?} else {
    /*@Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, ArmorStandEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isInvisible() && entity.getCustomName() != null) {
            if (this.getContextModel() instanceof ModelWithCape model) {
                var customName = entity.getCustomName();
                boolean isBaby = entity.isBaby();
    *///?}
                if (customName != null && StringUtils.matchASName(customName.getString()).get(1).contains("C")) {
                    PASModelData modelData = PASModelData.getByName(customName.getString());
                    Identifier capeTexture = modelData.cape;

                    matrixStack.push();
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(10.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F));
                    matrixStack.translate(0.0F, 0.02F, -0.12F);
                    if (isBaby) {
                        matrixStack.translate(0.0F, 0.7F, 0.2F);
                        matrixStack.scale(0.5F, 0.5F, 0.5F);
                    } else {
                        matrixStack.scale(1.0F, 1.0F, 1.0F);
                    }

                    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(capeTexture));
                    model.getCape().visible = true;
                    model.getCape().render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

                    matrixStack.pop();
                }

            }
        }

    }
}
