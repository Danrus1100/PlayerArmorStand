package com.danrus.pas.render.common;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.mc.Id;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasRenderer {

    public static Identifier WOOD = Id.vanilla(
            //? <26.1
            //"textures/entity/armorstand/wood.png"
            //? >=26.1
            "textures/entity/armorstand/armorstand.png"
    );
    public static Identifier STEVE = Id.vanilla("textures/entity/player/wide/steve.png");

    private final PlayerArmorStandModel model;
    @Nullable
    private final PlayerArmorStandModel smallModel;

    public PasRenderer(PlayerArmorStandModel model) {
        this(model, null);
    }

    public PasRenderer(PlayerArmorStandModel model, @Nullable PlayerArmorStandModel smallModel) {
        this.model = model;
        this.smallModel = smallModel;
    }

    public void draw(SkinData skinData, @Nullable CapeData capeData, NameInfo info, PasRenderContext context, PasModelSettings settings, PoseStack poseStack, int packedLight, int packedOverlay) {
        if (info.lolmeme() != null) {
            setupAnim(info, model, settings);
            drawMeme(info.lolmeme(), context, poseStack, packedLight, packedOverlay);
            return;
        }
        PlayerArmorStandModel modelToRender = (settings.isSmall() && smallModel != null) ? smallModel : model;
        setupAnim(info, modelToRender, settings);
        for (ModelPart part : modelToRender.getOriginalParts()) {
            drawPart(poseStack, part, RenderTypes.entityCutout(WOOD), context, packedLight, packedOverlay);
        }
        boolean showDefaultSkin = info.isEmpty() || PlayerArmorStandModel.showArmorStandWhileDownload(Optional.of(skinData));
        Identifier location = showDefaultSkin ? STEVE : PasManager.getInstance().getSkinWithOverlayTexture(info);
        for (ModelPart part : modelToRender.getPlayerParts()) {
            drawPart(poseStack, part, RenderTypes.entityTranslucent(location), context, packedLight, packedOverlay);
        }

        if (settings.foil()) {
            for (ModelPart part : modelToRender.getOriginalParts()) {
                drawPart(poseStack, part, RenderTypes.glint(), context, packedLight, packedOverlay);
            }

            for (ModelPart part : modelToRender.getPlayerParts()) {
                drawPart(poseStack, part, RenderTypes.glint(), context, packedLight, packedOverlay);
            }
        }

        if (info.hasCape() && capeData != null && !capeData.getTexture().equals(CapeData.DEFAULT_TEXTURE)) {
            drawPart(poseStack, modelToRender.getCape(), RenderTypes.entityTranslucent(capeData.getTexture()), context, packedLight, packedOverlay);

            if (settings.foil()) {
                drawPart(poseStack, modelToRender.getCape(), RenderTypes.glint(), context, packedLight, packedOverlay);
            }
        }
    }

    private void drawMeme(@NotNull Identifier texture, PasRenderContext context, PoseStack poseStack, int packedLight, int packedOverlay) {
        drawPart(poseStack, model.getMemePart(), RenderTypes.entityCutout(texture), context, packedLight, packedOverlay);
    }

    private static void setupAnim(NameInfo info, PlayerArmorStandModel model, PasModelSettings settings) {
        model.setupAnim(settings.poseSettings().toRenderState(info), info);
    }

    private static void drawPart(PoseStack poseStack, ModelPart part, RenderType type, PasRenderContext context, int packedLight, int packedOverlay) {
        SubmitNodeCollector nodeCollector = context.getData(net.minecraft.client.renderer.SubmitNodeCollector.class,"collector");
        ModelPartSnapshot snapshot = ModelPartSnapshot.capture(part);
        nodeCollector.submitCustomGeometry(poseStack, type, (pose, vertexConsumer) -> {
            ModelPartSnapshot currentState = ModelPartSnapshot.capture(part);
            PoseStack renderPoseStack = new PoseStack();
            renderPoseStack.last().set(pose);

            try {
                snapshot.apply();
                part.render(renderPoseStack, vertexConsumer, packedLight, packedOverlay);
            } finally {
                currentState.apply();
            }
        });
    }

    public PlayerArmorStandModel getModel(boolean isSmall) {
        return isSmall && smallModel != null ? smallModel : model;
    }

    private record ModelPartSnapshot(List<ModelPartState> states) {

        private static ModelPartSnapshot capture(ModelPart root) {
            List<ModelPartState> states = new ArrayList<>();
            for (ModelPart part : root.getAllParts()) {
                states.add(new ModelPartState(part, part.storePose(), part.visible, part.skipDraw));
            }
            return new ModelPartSnapshot(states);
        }

        private void apply() {
            for (ModelPartState state : states) {
                state.apply();
            }
        }
    }

    private record ModelPartState(ModelPart part, PartPose pose, boolean visible, boolean skipDraw) {

        private void apply() {
            part.loadPose(pose);
            part.visible = visible;
            part.skipDraw = skipDraw;
        }
    }
}
