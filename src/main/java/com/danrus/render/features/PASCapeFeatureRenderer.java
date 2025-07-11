package com.danrus.render.features;

import com.danrus.PASModelData;
import com.danrus.managers.SkinManger;
import com.danrus.render.models.PASModel;
import com.danrus.utils.StringUtils;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
//?}

//TODO: поддержка поворота плаща

//? if >=1.21.2 {
public class PASCapeFeatureRenderer extends FeatureRenderer<ArmorStandEntityRenderState, PASModel> {
    public PASCapeFeatureRenderer(FeatureRendererContext<ArmorStandEntityRenderState, PASModel> context) {
//?} else {
/*public class PASCapeFeatureRenderer extends FeatureRenderer<ArmorStandEntity, PASModel> {
    public PASCapeFeatureRenderer(FeatureRendererContext<ArmorStandEntity, PASModel> context) {
*///?}
        super(context);
    }


    //? if >=1.21.2 {
    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, ArmorStandEntityRenderState state, float limbAngle, float limbDistance) {
        if (!state.invisible && state.customName != null) {
            var customName = state.customName;
            boolean isBaby = state.baby;


    //?} else {
    /*@Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, ArmorStandEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isInvisible() && entity.getCustomName() != null) {
            var customName = entity.getCustomName();
            boolean isBaby = entity.isBaby();
    *///?}
            List<String> nameData = StringUtils.matchASName(customName.getString());
            if (customName != null && nameData.get(1).contains("C")) {
                PASModelData modelData = SkinManger.getInstance().getData(customName);
                Identifier capeTexture;
                if (nameData.get(2) != "") {
                    capeTexture = Identifier.of("pas", "capes/" + nameData.get(2) + ".png");
                } else {
                    capeTexture = modelData.getCapeTexture();
                }

                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(10.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F));
                matrixStack.translate(0.0F, 0.02F, -0.12F);
                //? <=1.21.1 {
                /*if (isBaby) {
                    matrixStack.translate(0.0F, 0.7F, 0.2F);
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                } else {
                    matrixStack.scale(1.0F, 1.0F, 1.0F);
                }
                *///?} else {

                if (isBaby) {
                    matrixStack.translate(0.0F, -0.1F, 0.21F);
//                        matrixStack.scale(0.5F, 0.5F, 0.5F);
                } else {
                    matrixStack.scale(1.0F, 1.0F, 1.0F);
                }
                //?}

                PASModel model = this.getContextModel();

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(capeTexture));
                model.getCape().visible = true;
                model.getCape().render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

                matrixStack.pop();
            }
        }

    }
}
