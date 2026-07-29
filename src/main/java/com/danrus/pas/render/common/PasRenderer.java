package com.danrus.pas.render.common;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.mc.Id;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
    private final boolean inGui;

    public PasRenderer(PlayerArmorStandModel model) {
        this(model, null, true);
    }

    public PasRenderer(PlayerArmorStandModel model, @Nullable PlayerArmorStandModel smallModel, boolean inGui) {
        this.model = model;
        this.smallModel = smallModel;
        this.inGui = inGui;
    }

    public void submit(SkinData skinData, @Nullable CapeData capeData, NameInfo info, SubmitNodeCollector collector, PasModelSettings settings, PoseStack poseStack, int packedLight, int packedOverlay) {
        if (info.lolmeme() != null) {
            setupAnim(info, model, settings);
            drawMeme(info.lolmeme(), collector, poseStack, packedLight, packedOverlay);
            return;
        }
        if (inGui && info.shouldUpsideDown()) rotatePoseToUpsideDown(poseStack);
        PlayerArmorStandModel modelToRender = (settings.isSmall() && smallModel != null) ? smallModel : model;
        setupAnim(info, modelToRender, settings);
        for (ModelPart part : modelToRender.getOriginalParts()) {
            drawPart(poseStack, part, RenderTypes.entityCutout(WOOD), collector, packedLight, packedOverlay);
        }
        boolean showDefaultSkin = info.isEmpty() || PlayerArmorStandModel.showArmorStandWhileDownload(Optional.of(skinData));
        Identifier location = showDefaultSkin ? STEVE : PasManager.getInstance().getSkinWithOverlayTexture(info);
        for (ModelPart part : modelToRender.getPlayerParts()) {
            drawPart(poseStack, part, RenderTypes.entityTranslucent(location), collector, packedLight, packedOverlay);
        }

        if (settings.foil()) {
            for (ModelPart part : modelToRender.getOriginalParts()) {
                drawPart(poseStack, part, RenderTypes.glint(), collector, packedLight, packedOverlay);
            }

            for (ModelPart part : modelToRender.getPlayerParts()) {
                drawPart(poseStack, part, RenderTypes.glint(), collector, packedLight, packedOverlay);
            }
        }

        if (info.hasCape() && capeData != null && !capeData.getTexture().equals(CapeData.DEFAULT_TEXTURE)) {
            drawPart(poseStack, modelToRender.getCape(), RenderTypes.entityTranslucent(capeData.getTexture()), collector, packedLight, packedOverlay);

            if (settings.foil()) {
                drawPart(poseStack, modelToRender.getCape(), RenderTypes.glint(), collector, packedLight, packedOverlay);
            }
        }
    }

    private void rotatePoseToUpsideDown(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.975F, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
    }

    private void drawMeme(@NotNull Identifier texture, SubmitNodeCollector collector, PoseStack poseStack, int packedLight, int packedOverlay) {
        drawPart(poseStack, model.getMemePart(), RenderTypes.entitySolid(texture), collector, packedLight, packedOverlay);
    }

    private static void setupAnim(NameInfo info, PlayerArmorStandModel model, PasModelSettings settings) {
        model.setupModel(settings.poseSettings(), info);
    }

    private static void drawPart(PoseStack poseStack, ModelPart part, RenderType type, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay) {
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
                states.add(ModelPartState.capture(part));
            }
            return new ModelPartSnapshot(states);
        }

        private void apply() {
            for (ModelPartState state : states) {
                state.apply();
            }
        }
    }

    private record ModelPartState(
            ModelPart part,
            PartPose pose,
            float xScale,
            float yScale,
            float zScale,
            boolean visible,
            boolean skipDraw
    ) {

        private void apply() {
            part.loadPose(pose);
            part.xScale = xScale;
            part.yScale = yScale;
            part.zScale = zScale;
            part.visible = visible;
            part.skipDraw = skipDraw;
        }

        private static ModelPartState capture(ModelPart part) {
            return new ModelPartState(
                    part,
                    part.storePose(),
                    part.xScale,
                    part.yScale,
                    part.zScale,
                    part.visible,
                    part.skipDraw
            );
        }
    }
}
