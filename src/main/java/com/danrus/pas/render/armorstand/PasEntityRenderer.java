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
import net.minecraft.client.model.object.armorstand.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
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

        this.addLayer(new HumanoidArmorLayer(this, ArmorModelSet.bake(ModelLayers.ARMOR_STAND_ARMOR, context.getModelSet(), ArmorStandArmorModel::new), ArmorModelSet.bake(ModelLayers.ARMOR_STAND_SMALL_ARMOR, context.getModelSet(), ArmorStandArmorModel::new), context.getEquipmentRenderer()));
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new WingsLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    }

    @Override
    public @NotNull PasEntityRenderState createRenderState() {
        return new PasEntityRenderState();
    }

    @Override
    public void submit(PasEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        Optional<SkinData> data = PasManager.getInstance().getSkinDataManager().getData(state.info);
        if ((data.isEmpty() || data.get().getStatus() != DownloadStatus.COMPLETED && state.info.lolmeme() == null) || !PasConfig.getInstance().isEnableMod()) {
            this.model = DummyEntityModel.INSTANTS;
            armorStandRenderer.submit(state, poseStack, submitNodeCollector, cameraRenderState);
            return;
        }
        if (state.info.lolmeme() != null) {
            state.bodyRot = 0;
            state.yRot = 0;
            swapVanillaDraw(() -> {
                Quaternionf rotation = new Quaternionf(entityRenderDispatcher.camera.rotation());

                rotation = calculateOrientation(rotation);

                poseStack.pushPose();
                poseStack.mulPose(rotation);
                poseStack.translate(0, -1, 0);

                executeDraw(data.get(), state, PasRenderContext.create(submitNodeCollector), poseStack, state.lightCoords);

                poseStack.popPose();
            });

        } else {
            swapVanillaDraw(() -> executeDraw(data.get(), state, PasRenderContext.create(submitNodeCollector), poseStack, state.lightCoords));
        }

        this.model = pasRenderer.getModel(state.isSmall);
        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Override
    public @NotNull Identifier getTextureLocation(PasEntityRenderState renderState) {
        return armorStandRenderer.getTextureLocation(renderState);
    }

    private void swapVanillaDraw(Runnable draw) {
        ((DrawSwapper) this).pas$swapDrawer(draw);
    }

    private void executeDraw(SkinData data, PasEntityRenderState state, PasRenderContext context, PoseStack poseStack, int packedLight) {
        if (!state.isInvisible && !state.isInvisibleToPlayer) {
            pasRenderer.draw(
                    data,
                    PasManager.getInstance().getCapeDataManager().getData(state.info).orElse(null),
                    state.info,
                    context,
                    new PasModelSettings(new PasModelPoseSettings(state), false, state.isBaby),
                    poseStack,
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );
        }
    }
    private Quaternionf calculateOrientation(Quaternionf quaternion) {
        Camera camera = entityRenderDispatcher.camera;
        return quaternion.rotationYXZ(-0.017453292F * -cameraYrot(camera), ((float)Math.PI / 180F) * cameraXRot(camera), 0.0F);
    }

    private static float cameraYrot(Camera camera) {
        //? if < 1.21.11
        //return camera.getYRot() - 90.0F;
        //? if >= 1.21.11
        return camera.yRot() - 180.0F;
    }

    private static float cameraXRot(Camera camera) {
        //? if < 1.21.11
        //return -camera.getXRot();
        //? if >= 1.21.11
        return -camera.xRot();
    }

    @Override
    public void extractRenderState(ArmorStand livingEntity, PasEntityRenderState state, float partialTick) {
        armorStandRenderer.extractRenderState(livingEntity, state, partialTick);
        state.info = NameInfo.parse(livingEntity.getCustomName());
        state.customName = livingEntity.getCustomName();
    }
}
