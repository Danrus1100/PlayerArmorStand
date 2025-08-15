package com.danrus.pas.mixin;

import com.danrus.pas.config.ModConfig;
import com.danrus.pas.mixin.accessors.LivingEntityRendererAccessor;
import com.danrus.pas.render.ArmorStandCapeLayer;
import com.danrus.pas.render.PlayerArmorStandModel;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.SkinManger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin implements VersioningUtils.VersionlessArmorStandCape {

    @Unique
    private PlayerArmorStandModel model;

    //? if >= 1.21.4 {
    @Shadow @Mutable @Final
    private ArmorStandArmorModel smallModel;

    @Shadow @Mutable @Final
    private ArmorStandArmorModel bigModel;
    //?}

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.model = new PlayerArmorStandModel(context.bakeLayer(ModelLayers.ARMOR_STAND));
        ((LivingEntityRendererAccessor) this).invokeAddLayer(new ArmorStandCapeLayer(this));
        ((LivingEntityRendererAccessor) this).setModel(model);
        //? if >= 1.21.4 {
        this.bigModel = model;
        this.smallModel = new PlayerArmorStandModel(context.bakeLayer(ModelLayers.ARMOR_STAND_SMALL));
        //?}
    }

    @Inject(
            //? if <= 1.21.1 {
            /*method = "getTextureLocation(Lnet/minecraft/world/entity/decoration/ArmorStand;)Lnet/minecraft/resources/ResourceLocation;",
            *///?} else {
            method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;)Lnet/minecraft/resources/ResourceLocation;",
            //?}
            at = @At("RETURN"),
            cancellable = true
    )
    private void textureLocation(
            //? if <= 1.21.1 {
            /*net.minecraft.world.entity.decoration.ArmorStand
            *///?} else {
            net.minecraft.client.renderer.entity.state.ArmorStandRenderState
            //?}
                    armorStand, CallbackInfoReturnable<ResourceLocation> cir){
        if (VersioningUtils.getCustomName(armorStand) == null || !ModConfig.get().enableMod) {
            return;
        }
        cir.setReturnValue(SkinManger.getInstance().getSkinTexture(VersioningUtils.getCustomName(armorStand)));
    }

    @Inject(
            method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
            at = @At("HEAD")
    )
    private void pas$setupRotations(ArmorStandRenderState renderState, PoseStack poseStack, float f, float scale, CallbackInfo ci) {
        if (renderState.isUpsideDown) {
            poseStack.translate(0.0F, (renderState.boundingBoxHeight + 0.1F) / scale, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
    }
}
