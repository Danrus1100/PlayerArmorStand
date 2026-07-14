package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.common.PasModelPoseSettings;
import com.danrus.pas.render.common.PasModelSettings;
import com.danrus.pas.render.common.PasRenderContext;
import com.danrus.pas.render.common.PasRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Optional;

public class PasEntityRenderer extends EntityRenderer<ArmorStand, PasEntityRenderState> {

    private final ArmorStandRenderer armorStandRenderer;
    private final PasRenderer renderer;

    public PasEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = .0f;
        this.armorStandRenderer = new ArmorStandRenderer(context);
        this.renderer = new PasRenderer(new PlayerArmorStandModel(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE).bakeRoot()));
    }

    @Override
    public @NotNull PasEntityRenderState createRenderState() {
        return new PasEntityRenderState();
    }

    @Override
    public void render(PasEntityRenderState state, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        Optional<SkinData> data = PasManager.getInstance().getSkinDataManager().getData(state.info);
        if (data.isEmpty() || data.get().getStatus() != DownloadStatus.COMPLETED) {
            armorStandRenderer.render(state, poseStack, multiBufferSource, i);
            return;
        }
        if (state.info.lolmeme() != null) {
            state.bodyRot = 0;
            state.yRot = 0;

            Quaternionf rotation = new Quaternionf(entityRenderDispatcher.camera.rotation());

            rotation = calculateOrientation(rotation);

            poseStack.pushPose();

            poseStack.mulPose(rotation);

            poseStack.translate(0, 1, 0);

            executeDraw(data.get(), state, multiBufferSource, poseStack, i);

            poseStack.popPose();
        } else {
            executeDraw(data.get(), state, multiBufferSource, poseStack, i);
        }


        super.render(state, poseStack, multiBufferSource, i);
    }

    private void executeDraw(SkinData data, PasEntityRenderState state, MultiBufferSource multiBufferSource, PoseStack poseStack, int i) {
        renderer.draw(
                data,
                PasManager.getInstance().getCapeDataManager().getData(state.info).orElse(null),
                state.info,
                PasRenderContext.create(multiBufferSource),
                new PasModelSettings(new PasModelPoseSettings(state), false),
                poseStack,
                i,
                OverlayTexture.NO_OVERLAY
        );
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
        armorStandRenderer.extractRenderState  (livingEntity, state, partialTick);
        state.info = NameInfo.parse(livingEntity.getCustomName());
    }
}
