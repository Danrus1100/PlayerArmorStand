package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.render.common.PasModelPoseSettings;
import com.danrus.pas.render.common.PasRenderContext;
import com.danrus.pas.render.common.PasRenderer;
import com.danrus.pas.utils.Rl;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public abstract class PasSpecialModelRenderer implements SpecialModelRenderer<ItemRenderData> {

    protected final PasRenderer renderer;
    protected final PasModelPoseSettings state;

    protected PasSpecialModelRenderer(PlayerArmorStandModel model, PasModelPoseSettings state) {
        this.renderer = new PasRenderer(model);
        this.state = state;
    }
    @Override
    //? if <1.21.9 {
    public void render(@Nullable ItemRenderData argument, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        PasRenderContext context = new PasRenderContext().putData(bufferSource, "bufferSource");
    //?} else if >=1.21.9 <26.1 {
    /*public void submit(@Nullable ItemRenderData argument, ItemDisplayContext displayContext, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor){
        PasRenderContext context = new RenderContext().putData(nodeCollector, "collector").putData(outlineColor, "outlineColor");
    *///?} else {
    /*public void submit(@Nullable ItemRenderData argument, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor) {
        PasRenderContext context = new RenderContext().putData(submitNodeCollector, "collector").putData(outlineColor, "outlineColor");
    *///?}
        prepareDraw(argument, poseStack, );
    }

    abstract void prepareDraw(ItemRenderData argument, PoseStack poseStack, PasRenderContext context, int packedLight, int packedOverlay, boolean hasFoil);
}
