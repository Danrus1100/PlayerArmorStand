package com.danrus.pas.render.item;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.PasRenderContext;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
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

import java.util.HashMap;
import java.util.Map;

public abstract class PasSpecialModelRenderer implements SpecialModelRenderer<SkinData> {

    private static ResourceLocation WOOD = Rl.vanilla("textures/entity/armorstand/wood.png");
    private static ResourceLocation STEVE = Rl.vanilla("textures/entity/player/wide/steve.png");

    protected final PlayerArmorStandModel model;
    protected final ArmorStandSpecialRenderer.ArmorStandItemState state;

    protected PasSpecialModelRenderer(PlayerArmorStandModel model, ArmorStandSpecialRenderer.ArmorStandItemState state) {
        this.model = model;
        this.state = state;
    }
    @Override
    //? if <1.21.9 {
    public void render(@Nullable SkinData argument, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        PasRenderContext context = new RenderContext().putData(bufferSource, "bufferSource");
    //?} else {
    /*public void submit(@Nullable SkinData argument, ItemDisplayContext displayContext, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor){
        PasRenderContext context = new RenderContext().putData(nodeCollector, "collector").putData(outlineColor, "outlineColor");
    *///?}
        NameInfo currentInfo = argument != null ? argument.getInfo() : new NameInfo();
        prepareDraw(currentInfo, displayContext, poseStack, context, packedLight, packedOverlay, hasFoilType);
        RenderType base = RenderType.entityCutout(WOOD);
        for (ModelPart part : this.model.getOriginalParts()) {
            renderPart(poseStack, part, base, context, packedLight, packedOverlay);
        }
        boolean showDefaultSkin = currentInfo.isEmpty() || PlayerArmorStandModel.showArmorStandWhileDownload(argument);
        RenderType skinType = RenderType.entityTranslucent(showDefaultSkin ? STEVE : PasManager.getInstance().getSkinWithOverlayTexture(currentInfo));
        for (ModelPart part : this.model.getPlayerParts()) {
            renderPart(poseStack, part, skinType, context, packedLight, packedOverlay);
        }

        if (hasFoilType) {
            // Используем стандартный RenderType для эффекта зачарования
            // Он имеет специальный шейдер, который игнорирует текстуру и рисует свечение.
            RenderType glintType = RenderType.glint();

            // Нам нужно нарисовать модель еще раз, чтобы glintType покрыл все части,
            // которые должны светиться.

            // Сначала рисуем glint поверх деревянных частей
            for (ModelPart part : this.model.getOriginalParts()) {
                // Пакет освещения и наложения обычно игнорируется шейдером glint,
                // но лучше передать их для корректной работы конвейера.
                renderPart(poseStack, part, glintType, context, packedLight, packedOverlay);
            }

            // Затем рисуем glint поверх частей игрока (скина)
            for (ModelPart part : this.model.getPlayerParts()) {
                renderPart(poseStack, part, glintType, context, packedLight, packedOverlay);
            }
        }
    }

    abstract void prepareDraw(NameInfo argument, ItemDisplayContext displayContext, PoseStack poseStack, PasRenderContext context, int packedLight, int packedOverlay, boolean hasFoil);

    private static void renderPart(PoseStack poseStack, ModelPart part, RenderType type, PasRenderContext context, int packedLight, int packedOverlay) {
        //? if <1.21.9 {
        MultiBufferSource bufferSource = context.getData(MultiBufferSource.class,"bufferSource");
        VertexConsumer skinConsumer = bufferSource.getBuffer(type);
        part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        //?} else {
        /*net.minecraft.client.renderer.SubmitNodeCollector nodeCollector = context.getData(net.minecraft.client.renderer.SubmitNodeCollector.class,"collector");
        nodeCollector.submitModelPart(part, poseStack, type, packedLight, packedOverlay, null);
        *///?}
    }

    static class RenderContext implements PasRenderContext {

        private final Map<String, Object> contextMap = new HashMap<>(16);

        public <T> PasRenderContext putData(T data, String type) {
            if (contextMap.size() >= 16) {
                throw new IllegalStateException("RenderVersionContext can hold up to 16 data entries.");
            }
            if (data != null) {
                contextMap.put(type, data);
            }
            return this;
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
