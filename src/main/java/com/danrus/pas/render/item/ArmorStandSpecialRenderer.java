package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.render.PasRenderContext;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Set;

public class ArmorStandSpecialRenderer extends PasSpecialModelRenderer {

    public ArmorStandSpecialRenderer(PlayerArmorStandModel model, ArmorStandItemState state) {
        super(model, state);
    }

    @Override
    public void prepareDraw(NameInfo info, ItemDisplayContext displayContext, PoseStack poseStack, PasRenderContext context, int packedLight, int packedOverlay, boolean hasFoil) {
        ArmorStandRenderState renderState = state.toRenderState();
        ModUtils.setCustomName(renderState, Component.literal(info.compile()));
        renderState.showBasePlate = state.baseplate;
        model.setupAnim(renderState, true);
        model.setupForItem(state, info);

        poseStack.translate(0.5, 0.75, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.scale(0.5f, 0.5f, 0.5f);
    }

    public void getExtents(Set<Vector3f> output) {
        output.add(new Vector3f(0, 0, 0));
    }

    @Override
    public @Nullable NameInfo extractArgument(ItemStack stack) {
        return NameInfo.parse(stack.getCustomName());
    }

    public static record Unbaked(ArmorStandItemState state) implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        ArmorStandItemState.CODEC.optionalFieldOf("state", new ArmorStandItemState()).forGetter(ArmorStandSpecialRenderer.Unbaked::state)
                ).apply(instance, ArmorStandSpecialRenderer.Unbaked::new)
        );


        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            PlayerArmorStandModel pasModel = new PlayerArmorStandModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND));
            return new ArmorStandSpecialRenderer(pasModel, state);
        }

        //? if >=1.21.9 {
        /*@Override
        public @Nullable SpecialModelRenderer<?> bake(BakingContext context) {
            return bake(context.entityModelSet());
        }
        *///?}

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }

    public static class ArmorStandItemState {
        public static PasItemModelPart DEFAULT_HEAD = new PasItemModelPart(new Vector3f(0, 0, 0), PasItemModelPart.Mode.DYNAMIC);
        public static PasItemModelPart DEFAULT_BODY = new PasItemModelPart(new Vector3f(0, 0, 0));
        public static PasItemModelPart DEFAULT_LEFT_LEG = new PasItemModelPart(new Vector3f(-1, 0, -1));
        public static PasItemModelPart DEFAULT_RIGHT_LEG = new PasItemModelPart(new Vector3f(1, 0, 1));
        public static PasItemModelPart DEFAULT_LEFT_ARM = new PasItemModelPart(new Vector3f(-10, 0, -10), PasItemModelPart.Mode.INVISIBLE);
        public static PasItemModelPart DEFAULT_RIGHT_ARM = new PasItemModelPart(new Vector3f(-15, 0, 10), PasItemModelPart.Mode.INVISIBLE);

        public static Codec<ArmorStandItemState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PasItemModelPart.CODEC.optionalFieldOf("head", DEFAULT_HEAD).forGetter(state -> state.head),
                PasItemModelPart.CODEC.optionalFieldOf("body", DEFAULT_BODY).forGetter(state -> state.body),
                PasItemModelPart.CODEC.optionalFieldOf("left_leg", DEFAULT_LEFT_LEG).forGetter(state -> state.leftLeg),
                PasItemModelPart.CODEC.optionalFieldOf("right_leg", DEFAULT_RIGHT_LEG).forGetter(state -> state.rightLeg),
                PasItemModelPart.CODEC.optionalFieldOf("left_arm", DEFAULT_LEFT_ARM).forGetter(state -> state.leftArm),
                PasItemModelPart.CODEC.optionalFieldOf("right_arm", DEFAULT_RIGHT_ARM).forGetter(state -> state.rightArm),
                Codec.BOOL.optionalFieldOf("baseplate", true).forGetter(state -> state.baseplate)
        ).apply(instance, ArmorStandItemState::new));

        public PasItemModelPart head;
        public PasItemModelPart body;
        public PasItemModelPart leftLeg;
        public PasItemModelPart rightLeg;
        public PasItemModelPart leftArm;
        public PasItemModelPart rightArm;
        public boolean baseplate;


        public ArmorStandItemState() {
            this.head = DEFAULT_HEAD;
            this.body = DEFAULT_BODY;
            this.leftLeg = DEFAULT_LEFT_LEG;
            this.rightLeg = DEFAULT_RIGHT_LEG;
            this.leftArm = DEFAULT_LEFT_ARM;
            this.rightArm = DEFAULT_RIGHT_ARM;
            this.baseplate = true;
        }

        public ArmorStandItemState(
                PasItemModelPart head,
                PasItemModelPart body,
                PasItemModelPart leftLeg,
                PasItemModelPart rightLeg,
                PasItemModelPart leftArm,
                PasItemModelPart rightArm,
                boolean baseplate
        ) {
            this.head = head;
            this.body = body;
            this.leftLeg = leftLeg;
            this.rightLeg = rightLeg;
            this.leftArm = leftArm;
            this.rightArm = rightArm;
            this.baseplate = baseplate;
        }

        public ArmorStandRenderState toRenderState() {
            ArmorStandRenderState state = new ArmorStandRenderState();
            state.leftArmPose = this.leftArm.toRotations();
            state.rightArmPose = this.rightArm.toRotations();
            state.leftLegPose = this.leftLeg.toRotations();
            state.rightLegPose = this.rightLeg.toRotations();
            state.bodyPose = this.body.toRotations();
            state.headPose = this.head.toRotations();
            state.showBasePlate = this.baseplate;
            return state;
        }
    }
}
