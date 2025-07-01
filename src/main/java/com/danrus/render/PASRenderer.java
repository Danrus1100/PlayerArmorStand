package com.danrus.render;

import com.danrus.SkinManger;
import com.danrus.render.models.PlayerArmorStandModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PASRenderer extends LivingEntityRenderer<ArmorStandEntity, PlayerArmorStandModel> {

    public PASRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerArmorStandModel(ctx.getPart(EntityModelLayers.ARMOR_STAND)), 0.0F);
    }

    @Override
    public Identifier getTexture(ArmorStandEntity entity) {
        Text customName = entity.getCustomName();
        return SkinManger.getInstance().getSkinTexture(customName != null ? customName : Text.empty());
    }

    public void render(ArmorStandEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
}
