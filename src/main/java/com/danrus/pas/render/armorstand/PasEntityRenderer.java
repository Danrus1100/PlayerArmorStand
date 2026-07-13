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
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;

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
        renderer.draw(
                data.get(),
                PasManager.getInstance().getCapeDataManager().getData(state.info).orElse(null),
                state.info,
                PasRenderContext.create(multiBufferSource),
                new PasModelSettings(new PasModelPoseSettings(), false),
                poseStack,
                i,
                OverlayTexture.NO_OVERLAY
        );

        super.render(state, poseStack, multiBufferSource, i);
    }

    @Override
    public void extractRenderState(ArmorStand livingEntity, PasEntityRenderState state, float partialTick) {
        armorStandRenderer.extractRenderState  (livingEntity, state, partialTick);
        state.info = NameInfo.parse(livingEntity.getCustomName());
    }
}
