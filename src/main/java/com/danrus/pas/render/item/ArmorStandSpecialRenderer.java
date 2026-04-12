package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.render.common.PasModelPoseSettings;
import com.danrus.pas.render.common.PasModelSettings;
import com.danrus.pas.render.common.PasRenderContext;
import com.danrus.pas.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ArmorStandSpecialRenderer extends PasSpecialModelRenderer {

    public ArmorStandSpecialRenderer(PlayerArmorStandModel model, PasModelPoseSettings state) {
        super(model, state);
    }

    @Override
    public void prepareDraw(ItemRenderData data, PoseStack poseStack, PasRenderContext context, int packedLight, int packedOverlay, boolean hasFoil) {
        prepareModel(state, data.info());
        preparePose(poseStack);
    }

    //? >= 1.21.8 {
    /*@Override
    public void getExtents(
            //? if <1.21.11
            Set<Vector3f> output
            //? if >=1.21.11
            //Consumer<Vector3fc> output
    ) {
        PoseStack poseStack = new PoseStack();

        preparePose(poseStack);
        prepareModel(model, state, null);

        List<ModelPart> partsToMeasure = new ArrayList<>();
        partsToMeasure.addAll(model.getOriginalParts());
        partsToMeasure.addAll(model.getPlayerParts());

        for (ModelPart part : partsToMeasure) {
            if (part.visible) {
                part.getExtentsForGui(poseStack, output);
            }
        }
    }
    *///?}

    private static void preparePose(PoseStack poseStack) {
        poseStack.translate(0.5, 0.75, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.scale(0.5f, 0.5f, 0.5f);
    }

    private void prepareModel(PasModelSettings state, @Nullable NameInfo infoCandidate) {
        NameInfo info = infoCandidate != null ? infoCandidate : new NameInfo();
        ArmorStandRenderState renderState = state.toRenderState();
        ModUtils.setCustomName(renderState, Component.literal(info.compile()));
        renderState.showBasePlate = state.baseplate;
        model.setupAnim(renderState, true);
        model.setupVisibilityForItem(state, info);
    }


    // Use SkinData instead NameInfo because we need update ModelIdentityElement dynamically (look SpecialModelWrapper)
    // SkinData has DownloadStatus, so - using here:
    @Override
    public @Nullable ItemRenderData extractArgument(ItemStack stack) {
        NameInfo info = NameInfo.parse(stack.getCustomName());
        SkinData data = PasManager.getInstance().getSkinDataManager().getData(info);
        return new ItemRenderData(data, info);
    }

    public static record Unbaked(PasModelPoseSettings state) implements SpecialModelRenderer.Unbaked
            //? >= 26.1
            //<ItemRenderData>
    {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        PasModelPoseSettings.CODEC.optionalFieldOf("state", new PasModelPoseSettings()).forGetter(ArmorStandSpecialRenderer.Unbaked::state)
                ).apply(instance, ArmorStandSpecialRenderer.Unbaked::new)
        );


        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            PlayerArmorStandModel pasModel = new PlayerArmorStandModel(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE, new CubeDeformation(-0.001f)).bakeRoot()); // to fix glitchy textures on arms
            return new ArmorStandSpecialRenderer(pasModel, state);
        }

        //? if >=1.21.9 && <26.1 {
        /*@Override
        public @Nullable SpecialModelRenderer<?> bake(BakingContext context) {
            return bake(context.entityModelSet());
        }
        *///?} else if >=26.1 {
        /*@Override
        @SuppressWarnings("unchecked")
        public @Nullable SpecialModelRenderer<ItemRenderData> bake(BakingContext context) {
            return (SpecialModelRenderer<ItemRenderData>) bake(context.entityModelSet());
        }
        *///?}

        @Override
        public MapCodec
                //? <26.1 {
                <? extends SpecialModelRenderer.Unbaked>
                //?} else {
                /*<Unbaked>
                *///?}
        type() {
            return MAP_CODEC;
        }
    }
}
