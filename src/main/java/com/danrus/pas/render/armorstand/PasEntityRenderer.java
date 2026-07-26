package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.duck.DrawSwapper;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.common.PasModelPoseSettings;
import com.danrus.pas.render.common.PasModelSettings;
import com.danrus.pas.render.common.PasRenderContext;
import com.danrus.pas.render.common.PasRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Optional;

public class PasEntityRenderer extends LivingEntityRenderer<ArmorStand, PasEntityRenderState, ArmorStandArmorModel> {

    private final ArmorStandRenderer armorStandRenderer;
    private final PasRenderer pasRenderer;

    public PasEntityRenderer(EntityRendererProvider.Context context) {
        super(context, DummyEntityModel.INSTANTS, .0f);
        this.armorStandRenderer = new ArmorStandRenderer(context);
        this.pasRenderer = new PasRenderer(
                new PlayerArmorStandModel(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE).bakeRoot()),
                new PlayerArmorStandModel(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE).apply(HumanoidModel.BABY_TRANSFORMER).bakeRoot())
        );

        this.addLayer(new HumanoidArmorLayer(this, new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR)), new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_SMALL_INNER_ARMOR)), new ArmorStandArmorModel(context.bakeLayer(ModelLayers.ARMOR_STAND_SMALL_OUTER_ARMOR)), context.getEquipmentRenderer()));
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new WingsLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new CustomHeadLayer(this, context.getModelSet()));
    }

    @Override
    public @NotNull PasEntityRenderState createRenderState() {
        return new PasEntityRenderState();
    }

    @Override
    public void render(PasEntityRenderState state, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        Optional<SkinData> data = PasManager.getInstance().getSkinDataManager().getData(state.info);
        if ((data.isEmpty() || data.get().getStatus() != DownloadStatus.COMPLETED && state.info.lolmeme() == null) || !PasConfig.getInstance().isEnableMod()) {
            this.model = DummyEntityModel.INSTANTS;
            armorStandRenderer.render(state, poseStack, multiBufferSource, i);
            return;
        }
        if (state.info.lolmeme() != null) {
            swapVanillaDraw(() -> {
                state.bodyRot = 0;
                state.yRot = 0;

                Quaternionf rotation = new Quaternionf(entityRenderDispatcher.camera.rotation());

                rotation = calculateOrientation(rotation);

                poseStack.pushPose();
                poseStack.mulPose(rotation);
                poseStack.translate(0, 1, 0);

                executeDraw(data.get(), state, multiBufferSource, poseStack, i);

                poseStack.popPose();
            });

        } else {
            swapVanillaDraw(() -> executeDraw(data.get(), state, multiBufferSource, poseStack, i));
        }

        this.model = pasRenderer.getModel(state.isSmall);
        super.render(state, poseStack, multiBufferSource, i);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(PasEntityRenderState renderState) {
        return armorStandRenderer.getTextureLocation(renderState);
    }

    private void swapVanillaDraw(Runnable draw) {
        ((DrawSwapper) this).pas$swapDrawer(draw);
    }

    private void executeDraw(SkinData data, PasEntityRenderState state, MultiBufferSource multiBufferSource, PoseStack poseStack, int i) {
//        poseStack.pushPose();
//        if (state.hasPose(Pose.SLEEPING)) {
//            Direction direction = state.bedOrientation;
//            if (direction != null) {
//                float f = state.eyeHeight - 0.1F;
//                poseStack.translate((float)(-direction.getStepX()) * f, 0.0F, (float)(-direction.getStepZ()) * f);
//            }
//        }
//
//        float g = state.scale;
//        poseStack.scale(g, g, g);
//        setupRotations(state, poseStack, state.bodyRot, g);
//        poseStack.scale(-1.0F, -1.0F, 1.0F);
//        scale(poseStack);
//        poseStack.translate(0.0F, -1.501F, 0.0F);
//        renderer.getModel().setupAnim(state);

        if (!state.isInvisible && !state.isInvisibleToPlayer) {
            pasRenderer.draw(
                    data,
                    PasManager.getInstance().getCapeDataManager().getData(state.info).orElse(null),
                    state.info,
                    PasRenderContext.create(multiBufferSource),
                    new PasModelSettings(new PasModelPoseSettings(state), false, state.isBaby),
                    poseStack,
                    i,
                    OverlayTexture.NO_OVERLAY
            );
        }
//        poseStack.popPose();
    }
    private Quaternionf calculateOrientation(Quaternionf quaternion) {
        Camera camera = entityRenderDispatcher.camera;
        return quaternion.rotationYXZ(-0.017453292F * cameraYrot(camera), ((float)Math.PI / 180F) * cameraXRot(camera), 0.0F);
    }

    private static float cameraYrot(Camera camera) {
        //? if < 1.21.11
        return camera.getYRot() - 180.0F;
        //? if >= 1.21.11
        //return camera.yRot() - 180.0F;
    }

    private static float cameraXRot(Camera camera) {
        //? if < 1.21.11
        return -camera.getXRot();
        //? if >= 1.21.11
        //return -camera.xRot();
    }

    @Override
    public void extractRenderState(ArmorStand livingEntity, PasEntityRenderState state, float partialTick) {
        armorStandRenderer.extractRenderState(livingEntity, state, partialTick);
        state.info = NameInfo.parse(livingEntity.getCustomName());
        state.isBaby = livingEntity.isSmall();
    }
}
