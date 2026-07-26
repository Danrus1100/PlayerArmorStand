package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.render.common.PasModelPoseSettings;
import com.danrus.pas.render.common.PasModelSettings;
import com.danrus.pas.render.common.PasRenderContext;
import com.danrus.pas.render.common.PasRenderer;
import com.danrus.pas.utils.mc.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PasSpecialModelRenderer implements SpecialModelRenderer<ItemRenderData> {

    protected final PasRenderer renderer;
    protected final PasModelPoseSettings state;

    protected PasSpecialModelRenderer(PlayerArmorStandModel model, PasModelPoseSettings state) {
        this.renderer = new PasRenderer(model);
        this.state = state;
    }
    @Override
    //? if <26.1 {
    /*public void submit(@Nullable ItemRenderData argument, ItemDisplayContext displayContext, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor){
        PasRenderContext context = new PasRenderContext().putData(nodeCollector, "collector").putData(outlineColor, "outlineColor");
    *///?} else {
    public void submit(@Nullable ItemRenderData argument, PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor) {
        PasRenderContext context = new PasRenderContext().putData(submitNodeCollector, "collector").putData(outlineColor, "outlineColor");
    //?}
        SkinData skin;
        CapeData cape;
        NameInfo info;

        if (argument != null) {
            skin = argument.skinData();
            cape = argument.capeData();
            info = argument.info();
        } else {
            skin = new SkinData();
            cape = null;
            info = NameInfo.EMPTY;
        }

        preparePose(poseStack);
        prepareModel(info);
        renderer.draw(skin, cape, info, context, new PasModelSettings(state, hasFoilType, false), poseStack, packedLight, packedOverlay);
    }

    @Override
    public void getExtents(@NonNull Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();

        preparePose(poseStack);
        prepareModel(null);

        List<ModelPart> partsToMeasure = new ArrayList<>();
        partsToMeasure.addAll(renderer.getModel(false).getOriginalParts());
        partsToMeasure.addAll(renderer.getModel(false).getPlayerParts());
        partsToMeasure.add(renderer.getModel(false).getMemePart());

        for (ModelPart part : partsToMeasure) {
            if (part.visible) {
                part.getExtentsForGui(poseStack, output);
            }
        }
    }

    private static void preparePose(PoseStack poseStack) {
        poseStack.translate(0.5, 0.75, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.scale(0.5f, 0.5f, 0.5f);
    }

    private void prepareModel(@Nullable NameInfo infoCandidate) {
        NameInfo info = infoCandidate != null ? infoCandidate : new NameInfo();
        var model = renderer.getModel(false);
        ArmorStandRenderState renderState = state.toRenderState(info);
        renderState.nameTag = Component.literal(info.compileFast());
        renderState.showBasePlate = state.baseplate;
        model.setupAnim(renderState, info, true);
        model.setupVisibilityForItem(state, info);
    }

    // Use SkinData instead NameInfo because we need update ModelIdentityElement dynamically (look SpecialModelWrapper)
    // SkinData has DownloadStatus, so - using here:
    @Override
    public @Nullable ItemRenderData extractArgument(ItemStack stack) {
        NameInfo info = NameInfo.parse(stack.getCustomName());
        var skin = PasManager.getInstance().getSkinDataManager().getData(info);
        var cape = PasManager.getInstance().getCapeDataManager().getData(info);
        return new ItemRenderData(skin.orElse(new SkinData()), cape.orElse(null), info);
    }

    public static record Unbaked(PasModelPoseSettings state) implements SpecialModelRenderer.Unbaked
            //? >= 26.1
            <ItemRenderData>
    {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        PasModelPoseSettings.CODEC.optionalFieldOf("state", new PasModelPoseSettings()).forGetter(Unbaked::state)
                ).apply(instance, Unbaked::new)
        );


        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            PlayerArmorStandModel pasModel = new PlayerArmorStandModel(PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE, new CubeDeformation(-0.001f)).bakeRoot()); // to fix glitchy textures on arms
            return new PasSpecialModelRenderer(pasModel, state);
        }

        //? <26.1 {
        /*@Override
        public @Nullable SpecialModelRenderer<?> bake(BakingContext context) {
            return bake(context.entityModelSet());
        }
        *///?} else {
        @Override
        @SuppressWarnings("unchecked")
        public @Nullable SpecialModelRenderer<ItemRenderData> bake(BakingContext context) {
            return (SpecialModelRenderer<ItemRenderData>) bake(context.entityModelSet());
        }
        //?}

        @Override
        public MapCodec
            //? <26.1 {
            /*<? extends SpecialModelRenderer.Unbaked>
            *///?} else {
            <Unbaked>
             //?}
        type() {
            return MAP_CODEC;
        }
    }
}
