package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.PasRenderContext;
import com.danrus.pas.render.armorstand.ArmorStandCapeLayer;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class PasSpecialModelRenderer implements SpecialModelRenderer<NameInfo> {

    protected final PlayerArmorStandModel model;
    protected final ArmorStandSpecialRenderer.ArmorStandItemState state;

    protected PasSpecialModelRenderer(PlayerArmorStandModel model, ArmorStandSpecialRenderer.ArmorStandItemState state) {
        this.model = model;
        this.state = state;
    }

    //? if <1.21.9 {
    @Override
    public void render(@Nullable NameInfo argument, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        NameInfo currentInfo = argument != null ? argument : new NameInfo();
        RenderContext context = new RenderContext();
        context.putData(bufferSource, "bufferSource");
        prepareDraw(currentInfo, displayContext, poseStack, context, packedLight, packedOverlay, hasFoilType);
        RenderType base = RenderType.entityCutout(SkinData.DEFAULT_TEXTURE);
        for (ModelPart part : this.model.getOriginalParts()) {
            VertexConsumer skinConsumer = bufferSource.getBuffer(base);
            part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        }
        RenderType skinType = RenderType.entityTranslucent(currentInfo.isEmpty() ? SkinData.DEFAULT_TEXTURE : PasManager.getInstance().getSkinWithOverlayTexture(currentInfo));
        for (ModelPart part : this.model.getPlayerParts()) {
            VertexConsumer skinConsumer = bufferSource.getBuffer(skinType);
            part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        }
        if (state.cape) {
            ResourceLocation capeTexture = PasManager.getInstance().getCapeWithOverlayTexture(currentInfo);
            RenderType capeType = RenderType.entityCutout(capeTexture);
            VertexConsumer capeConsumer = bufferSource.getBuffer(capeType);
            ArmorStandCapeLayer.prepareCapePose(poseStack, false);
            this.model.getCape().render(poseStack, capeConsumer, packedLight, packedOverlay);
        }

    }
    //?} else {
    /*@Override
    public void submit(@Nullable NameInfo argument, ItemDisplayContext displayContext, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor){
        NameInfo currentInfo = argument != null ? argument : new NameInfo();
        RenderContext context = new RenderContext();
        context.putData(nodeCollector, "collector");
        context.putData(outlineColor, "outlineColor");
        prepareDraw(currentInfo, displayContext, poseStack, context, packedLight, packedOverlay, hasFoil);

        RenderType base = RenderType.entityCutout(SkinData.DEFAULT_TEXTURE);
        for (ModelPart part : this.model.getOriginalParts()) {
            nodeCollector.submitModelPart(part, poseStack, base, packedLight, packedOverlay, null);
        }
        RenderType skinType = RenderType.entityTranslucent(currentInfo.isEmpty() ? SkinData.DEFAULT_TEXTURE : PasManager.getInstance().getSkinWithOverlayTexture(currentInfo));
        for (ModelPart part : this.model.getPlayerParts()) {
            nodeCollector.submitModelPart(part, poseStack, skinType, packedLight, packedOverlay, null);
        }
    }
    *///?}

    abstract void prepareDraw(NameInfo argument, ItemDisplayContext displayContext, PoseStack poseStack, PasRenderContext context, int packedLight, int packedOverlay, boolean hasFoil);

    class RenderContext implements PasRenderContext {

        private final Map<String, Object> contextMap = new HashMap<>(16);

        public <T> void putData(T data, String type) {
            if (contextMap.size() >= 16) {
                throw new IllegalStateException("RenderVersionContext can hold up to 16 data entries.");
            }
            if (data != null) {
                contextMap.put(type, data);
            }
        }

        public <T> T getData(Class<T> clazz, String type) {
            try {
                if (!contextMap.containsKey(type)) {
                    throw new IllegalArgumentException("No data found for class: " + clazz.getName());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error retrieving data for class: " + clazz.getName(), e);
            }
            return clazz.cast(contextMap.get(type));
        }
    }
}
