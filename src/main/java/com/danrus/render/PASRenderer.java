package com.danrus.render;

import com.danrus.SkinManger;
import com.danrus.render.features.PASCapeFeatureRenderer;
import com.danrus.render.models.PASModel;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

//? if >= 1.21.2 {
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
//?}

//? if >= 1.21.4
import net.minecraft.client.item.ItemModelManager;

//? if >1.21.1 && <1.21.5 {
/*import net.minecraft.item.ModelTransformationMode;
*///?}

import java.util.List;
//? if <= 1.21.1 {
/*public class PASRenderer extends EntityRenderer<ArmorStandEntity> implements FeatureRendererContext<ArmorStandEntity, PASModel> {

    public PASModel model;
    protected final List<FeatureRenderer<ArmorStandEntity, PASModel>> features = Lists.newArrayList();

    public PASRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new PASModel(ctx.getPart(EntityModelLayers.ARMOR_STAND));
        this.addFeature(new ArmorFeatureRenderer(this, new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_OUTER_ARMOR)), ctx.getModelManager()));
        this.addFeature(new HeldItemFeatureRenderer(this, ctx.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
        this.addFeature(new PASCapeFeatureRenderer(this));
    }

    public final boolean addFeature(FeatureRenderer<ArmorStandEntity, PASModel> feature) {
        return this.features.add(feature);
    }

    protected boolean isVisible(ArmorStandEntity entity) {
        return !entity.isInvisible();
    }

    public static int getOverlay(ArmorStandEntity entity, float whiteOverlayProgress) {
        return OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), OverlayTexture.getV(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    @Override
    public Identifier getTexture(ArmorStandEntity entity) {
        Text customName = entity.getCustomName();
        if (customName == null || entity.isInvisible()) {
            return Identifier.of("minecraft", "textures/entity/armorstand/wood.png");
        }
        return SkinManger.getInstance().getSkinTexture(customName);
    }

    protected void setupTransforms(ArmorStandEntity armorStandEntity, MatrixStack matrixStack, float g, float h) {
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - g));
        float j = (float)(armorStandEntity.getWorld().getTime() - armorStandEntity.lastHitTime) + h;
        if (j < 5.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(j / 1.5F * (float)Math.PI) * 3.0F));
        }

    }

    protected boolean hasLabel(ArmorStandEntity armorStandEntity) {
        double d = this.dispatcher.getSquaredDistanceToCamera(armorStandEntity);
        float f = armorStandEntity.isInSneakingPose() ? 32.0F : 64.0F;
        return d >= (double)(f * f) ? false : armorStandEntity.isCustomNameVisible();
    }

    @Override
    public void render(ArmorStandEntity armorStand, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        this.model.riding = armorStand.hasVehicle();
        this.model.child = armorStand.isBaby();
        float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, armorStand.prevBodyYaw, armorStand.bodyYaw);
        //? if >= 1.20.6 {
        float scale = armorStand.getScale();
        //?} else {
        /^float scale = 1F;
        ^///?}
        boolean isInvisibleToPlayer =  armorStand.isInvisibleTo(MinecraftClient.getInstance().player);
*///?} else {
public class PASRenderer extends EntityRenderer<ArmorStandEntity, ArmorStandEntityRenderState> implements FeatureRendererContext<ArmorStandEntityRenderState, PASModel> {

    //? if >= 1.21.4 {
    protected final ItemModelManager itemModelResolver;
    //?} else {
    /*public ItemRenderer itemRenderer;
    *///?}
    public PASModel model;
    public PASModel normalModel;
    public PASModel smallModel;
    protected final List<FeatureRenderer<ArmorStandEntityRenderState, PASModel>> features = Lists.newArrayList();

    public PASRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.normalModel = new PASModel(ctx.getPart(EntityModelLayers.ARMOR_STAND));
        this.smallModel = new PASModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_SMALL));
        this.model = normalModel;
        //? if >= 1.21.4 {
        this.itemModelResolver = ctx.getItemModelManager();
        this.addFeature(new ArmorFeatureRenderer(this, new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_OUTER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_SMALL_INNER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_SMALL_OUTER_ARMOR)), ctx.getEquipmentRenderer()));
        this.addFeature(new HeldItemFeatureRenderer(this));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getEntityModels(), ctx.getEquipmentRenderer()));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getEntityModels()));
        //?} else {
        /*this.itemRenderer = ctx.getItemRenderer();
        this.addFeature(new ArmorFeatureRenderer(this, new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_INNER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_OUTER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_SMALL_INNER_ARMOR)), new ArmorStandArmorEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND_SMALL_OUTER_ARMOR)), ctx.getEquipmentRenderer()));
        this.addFeature(new HeldItemFeatureRenderer(this, ctx.getItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getModelLoader(), ctx.getEquipmentRenderer()));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getModelLoader(), ctx.getItemRenderer()));
         *///?}
        this.addFeature(new PASCapeFeatureRenderer(this));
    }

    public final boolean addFeature(FeatureRenderer<ArmorStandEntityRenderState, PASModel> feature) {
        return this.features.add(feature);
    }

    @Override
    public ArmorStandEntityRenderState createRenderState() {
        return new ArmorStandEntityRenderState();
    }

    protected boolean isVisible(ArmorStandEntityRenderState state) {
        return !state.invisible;
    }

    protected boolean hasLabel(ArmorStandEntity armorStandEntity, double d) {
        return armorStandEntity.isCustomNameVisible();
    }

    protected void setupTransforms(ArmorStandEntityRenderState armorStandEntityRenderState, MatrixStack matrixStack, float f, float tickDelta) {
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f));
        if (armorStandEntityRenderState.timeSinceLastHit < 5.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(armorStandEntityRenderState.timeSinceLastHit / 1.5F * (float)Math.PI) * 3.0F));
        }
    }

    public static int getOverlay(LivingEntityRenderState state, float whiteOverlayProgress) {
        return OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), OverlayTexture.getV(state.hurt));
    }

    public void updateRenderState(ArmorStandEntity livingEntity, ArmorStandEntityRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        //? if >= 1.21.5 {
        float g = MathHelper.lerpAngleDegrees(f, livingEntity.lastHeadYaw, livingEntity.headYaw);
        livingEntityRenderState.bodyYaw = MathHelper.lerpAngleDegrees(f, livingEntity.lastBodyYaw, livingEntity.bodyYaw);
        livingEntityRenderState.yaw = MathHelper.wrapDegrees(g - livingEntityRenderState.bodyYaw);
        livingEntityRenderState.pitch = livingEntity.getLerpedPitch(f);
        livingEntityRenderState.customName = livingEntity.getCustomName();
        if (livingEntityRenderState.flipUpsideDown) {
            livingEntityRenderState.pitch *= -1.0F;
            livingEntityRenderState.yaw *= -1.0F;
        }

        //?} else {
        /*float g = MathHelper.lerpAngleDegrees(f, livingEntity.prevHeadYaw, livingEntity.headYaw);
        livingEntityRenderState.bodyYaw = MathHelper.lerpAngleDegrees(f, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
        livingEntityRenderState.yawDegrees = MathHelper.wrapDegrees(g - livingEntityRenderState.bodyYaw);
        livingEntityRenderState.pitch = livingEntity.getLerpedPitch(f);
        livingEntityRenderState.customName = livingEntity.getCustomName();
        if (livingEntityRenderState.flipUpsideDown) {
            livingEntityRenderState.pitch *= -1.0F;
            livingEntityRenderState.yawDegrees *= -1.0F;
        }
        *///?}

        livingEntityRenderState.baseScale = livingEntity.getScale();
        livingEntityRenderState.ageScale = livingEntity.getScaleFactor();
        livingEntityRenderState.pose = livingEntity.getPose();
        livingEntityRenderState.sleepingDirection = livingEntity.getSleepingDirection();
        if (livingEntityRenderState.sleepingDirection != null) {
            livingEntityRenderState.standingEyeHeight = livingEntity.getEyeHeight(EntityPose.STANDING);
        }

        livingEntityRenderState.baby = livingEntity.isBaby();
        livingEntityRenderState.touchingWater = livingEntity.isTouchingWater();
        livingEntityRenderState.usingRiptide = livingEntity.isUsingRiptide();
        livingEntityRenderState.hurt = livingEntity.hurtTime > 0 || livingEntity.deathTime > 0;
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
        livingEntityRenderState.equippedHeadStack = itemStack.copy();
        livingEntityRenderState.mainArm = livingEntity.getMainArm();

        livingEntityRenderState.deathTime = livingEntity.deathTime > 0 ? (float)livingEntity.deathTime + f : 0.0F;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        livingEntityRenderState.invisibleToPlayer = livingEntityRenderState.invisible && livingEntity.isInvisibleTo(minecraftClient.player);
        livingEntityRenderState.hasOutline = minecraftClient.hasOutline(livingEntity);

        //? if > 1.21.2 {
        ItemStack HeaditemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
        Item var8 = HeaditemStack.getItem();
        if (var8 instanceof BlockItem blockItem) {
            Block var12 = blockItem.getBlock();
            if (var12 instanceof AbstractSkullBlock abstractSkullBlock) {
                livingEntityRenderState.wearingSkullType = abstractSkullBlock.getSkullType();
                livingEntityRenderState.wearingSkullProfile = (ProfileComponent)itemStack.get(DataComponentTypes.PROFILE);
                livingEntityRenderState.headItemRenderState.clear();
            }
        }
        //?} else {
        /*livingEntityRenderState.equippedHeadItemModel = this.itemRenderer.getModel(itemStack, livingEntity, ModelTransformationMode.HEAD);
        ItemStack itemStack2 = livingEntity.getStackInArm(Arm.RIGHT);
        ItemStack itemStack3 = livingEntity.getStackInArm(Arm.LEFT);

        livingEntityRenderState.rightHandStack = itemStack2.copy();
        livingEntityRenderState.leftHandStack = itemStack3.copy();
        livingEntityRenderState.rightHandItemModel = this.itemRenderer.getModel(itemStack2, livingEntity, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND);
        livingEntityRenderState.leftHandItemModel = this.itemRenderer.getModel(itemStack3, livingEntity, ModelTransformationMode.THIRD_PERSON_LEFT_HAND);
        *///?}
    }

    public Identifier getTexture(ArmorStandEntityRenderState state) {
        Text customName = state.customName;
        if (customName == null || state.invisible) {
            return Identifier.of("minecraft", "textures/entity/armorstand/wood.png");
        }
        return SkinManger.getInstance().getSkinTexture(customName);
    }


    public void render(ArmorStandEntityRenderState armorStand, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        this.model = armorStand.baby ? smallModel : normalModel;
        float scale = armorStand.baseScale;
        float bodyYaw = armorStand.bodyYaw;
        boolean isInvisibleToPlayer = armorStand.invisibleToPlayer;
        float tickDelta = 0.0F;
//?}
        matrixStack.push();

        //? if >= 1.20.6 {
        matrixStack.scale(scale-0.05F, scale-0.05F, scale-0.05F);
        //?} else {
        /*matrixStack.scale(0.95F, 0.95F, 0.95F);
        *///?}

        this.setupTransforms(armorStand, matrixStack, bodyYaw, tickDelta);

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0.0F, -1.501F, 0.0F);

        this.model.setAngles(armorStand);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean visible = this.isVisible(armorStand);
        boolean bl2 = !visible && !isInvisibleToPlayer;
        RenderLayer renderLayer = RenderLayer.getItemEntityTranslucentCull(getTexture(armorStand));
        if (renderLayer != null && visible) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int q = getOverlay(armorStand, 0.0F);
            //? if >= 1.21.1 {
            this.model.render(matrixStack, vertexConsumer, light, q, bl2 ? 654311423 : -1);
            //?} else {
            /*this.model.render(matrixStack, vertexConsumer, light, q, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
            *///?}
        }

        //? if >= 1.21.2 {
        for(FeatureRenderer<ArmorStandEntityRenderState, PASModel> featureRenderer : this.features) {
            //? if >= 1.21.5 {
            float yaw = armorStand.yaw;
            //?} else {
            /*float yaw = armorStand.yawDegrees;
            *///?}
            featureRenderer.render(matrixStack, vertexConsumerProvider, light, armorStand, yaw, armorStand.pitch);
        }
        matrixStack.pop();
        super.render(armorStand, matrixStack, vertexConsumerProvider, light);
        //?} else {

        /*for (FeatureRenderer<ArmorStandEntity, PASModel> featureRenderer : this.features) {
            featureRenderer.render(matrixStack, vertexConsumerProvider, light, armorStand, 0.0F, 0.0F, tickDelta, 0.0F, 0.0F, 0.0F);
        }
        matrixStack.pop();
        super.render(armorStand, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        *///?}
    }

    @Override
    public PASModel getModel() {
        return model;
    }
}