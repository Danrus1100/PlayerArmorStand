package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.mixin.accessors.LivingEntityRendererAccessor;
import com.danrus.pas.render.armorstand.ArmorStandCapeLayer;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin<S extends LivingEntityRenderState> implements ModUtils.VersionlessArmorStandCape {

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

    @WrapOperation(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    )
    private void pas$renderLol(ArmorStandRenderer instance, S state, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Operation<Void> original) {
        // 1. Получаем кастомное имя из состояния рендера
        NameInfo info = NameInfo.parse(state.customName != null ? state.customName.getString() : "");

        if (info.lolmeme != null) {
            // 2. Получаем диспетчер рендера для доступа к камере

            state.bodyRot = 0;
            state.yRot = 0;

            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

            // 3. Вычисляем поворот камеры (billboarding)
            // В Minecraft 1.21+ используется JOML Quaternionf
            Quaternionf rotation = new Quaternionf(dispatcher.cameraOrientation());

            rotation = calculateOrientation(dispatcher, rotation);

            // 4. Применяем трансформацию
            // Чтобы картинка всегда смотрела на игрока, нужно инвертировать или подстроить углы
            poseStack.pushPose(); // Сохраняем состояние

            // Убираем вращение самой сущности и заменяем его на вращение камеры
            poseStack.mulPose(rotation);

            poseStack.translate(0, 1, 0);

            // Если нужно, чтобы картинка не наклонялась по вертикали (только по горизонтали):
            // poseStack.mulPose(Axis.YP.rotationDegrees(-dispatcher.camera.getYRot()));

            // Вызываем рендер модели
            original.call(instance, state, poseStack, multiBufferSource, i);

            poseStack.popPose(); // Восстанавливаем состояние
        } else {
            original.call(instance, state, poseStack, multiBufferSource, i);
        }
    }

    private Quaternionf calculateOrientation(EntityRenderDispatcher entityRenderDispatcher, Quaternionf quaternion) {
        Camera camera = entityRenderDispatcher.camera;
        return quaternion.rotationYXZ(-0.017453292F * cameraYrot(camera), ((float)Math.PI / 180F) * cameraXRot(camera), 0.0F);
    }

    private static float cameraYrot(Camera camera) {
        return camera.getYRot() - 180.0F;
    }

    private static float cameraXRot(Camera camera) {
        return -camera.getXRot();
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
        if (ModUtils.getCustomName(armorStand) == null || !PasConfig.getInstance().isEnableMod()) {
            return;
        }
        cir.setReturnValue(PasManager.getInstance().getSkinWithOverlayTexture(NameInfo.parse(ModUtils.getCustomName(armorStand))));
    }

    //? if >= 1.21.4 {
    @Inject(
            method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
            at = @At("HEAD")
    )
    private void pas$setupRotations(net.minecraft.client.renderer.entity.state.ArmorStandRenderState renderState, PoseStack poseStack, float f, float scale, CallbackInfo ci) {
        if (renderState.isUpsideDown && PasConfig.getInstance().isEnableMod() && PasConfig.getInstance().isShowEasterEggs()) {
            poseStack.translate(0.0F, (renderState.boundingBoxHeight + 0.1F) / scale, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
    }
    //?} else {

    /*//? if < 1.21.1 {
    /^@Inject(
            method = "setupRotations(Lnet/minecraft/world/entity/decoration/ArmorStand;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At("HEAD")
    )
    private void pas$setupRotations(ArmorStand entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci){
    ^///?} else {
    @Inject(
            method = "setupRotations(Lnet/minecraft/world/entity/decoration/ArmorStand;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V",
            at = @At("HEAD")
    )
    private void pas$setupRotations(ArmorStand entityLiving, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci){
    //?}
        if (!PasConfig.getInstance().isEnableMod() || ModUtils.getCustomName(entityLiving) == null) {
            return;
        }

        NameInfo info = NameInfo.parse(entityLiving.getCustomName().getString());

        if (info.base().equalsIgnoreCase("Dinnerbone")
        || info.base().equalsIgnoreCase("Grumm")) {
            poseStack.translate(0.0F, entityLiving.getBbHeight() - 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
    }

    *///?}

    //? >=1.21.9 {
    /*@Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ArmorStand;Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;F)V",
            at = @At("RETURN")
    )
    private void setCustomName1219(ArmorStand armorStand, net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandRenderState, float f, CallbackInfo ci) {
        ((com.danrus.pas.extenders.ArmorStandRenderStateExtender) armorStandRenderState).pas$setCustomName(armorStand.getCustomName());
    }
    *///?}
}
