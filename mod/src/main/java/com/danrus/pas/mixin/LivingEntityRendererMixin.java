package com.danrus.pas.mixin;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.client.PasPostRenderEvent;
import com.danrus.pas.api.event.client.PasPreRenderEvent;
import com.danrus.pas.api.event.client.PasRenderTypePrepareEvent;
import com.danrus.pas.core.renderstate.ArmorStandCapture;
import com.danrus.pas.mixin.accessor.LivingEntityRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends
        //? if <= 1.21.1 {
        /*LivingEntity
         *///?} else {
        net.minecraft.client.renderer.entity.state.LivingEntityRenderState
        //?}
        , M extends EntityModel<T>> {

        @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
        )
        private void pas$renderPre(
                //? if <= 1.21.1 {
                /*LivingEntity
                 *///?} else {
                net.minecraft.client.renderer.entity.state.LivingEntityRenderState
                //?}
                armorStandCaptureCandidate, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci)
        {
            if (armorStandCaptureCandidate instanceof ArmorStandCapture armorStandCapture) {
                ArmorStand armorStand = armorStandCapture.pas$getArmorStand();

                if (armorStand == null) {
                    return; // Prevent rendering if armor stand is null
                }

                String customName = armorStand.getCustomName() != null ? armorStand.getCustomName().getString() : "";

                PasApi.postEvent(new PasPreRenderEvent(
                        armorStand, customName, poseStack, multiBufferSource,
                        () -> ((LivingEntityRenderer) (Object) this).getModel(),
                        (model) -> ((LivingEntityRendererAccessor) this).pas$setModel(model)
                ));
            }
        }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("TAIL")
    )
    private void pas$renderPost(
            //? if <= 1.21.1 {
            /*LivingEntity
             *///?} else {
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState
            //?}
                    armorStandCaptureCandidate, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci)
    {
        if (armorStandCaptureCandidate instanceof ArmorStandCapture armorStandCapture) {
            ArmorStand armorStand = armorStandCapture.pas$getArmorStand();

            if (armorStand == null) {
                return; // Prevent rendering if armor stand is null
            }

            String customName = armorStand.getCustomName() != null ? armorStand.getCustomName().getString() : "";

            PasApi.postEvent(new PasPostRenderEvent(
                    armorStand, customName, poseStack, multiBufferSource,
                    () -> ((LivingEntityRenderer) (Object) this).getModel()
            ));
        }
    }

    @Inject(
            method = "getRenderType",
            at = @At("RETURN"),
            cancellable = true
    )
    private void pas$getRenderType(
            //? if <= 1.21.1 {
            /*LivingEntity
             *///?} else {
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState
            //?}
                    armorStandCaptureCandidate, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing, CallbackInfoReturnable<RenderType> cir)
    {
        if (armorStandCaptureCandidate instanceof ArmorStandCapture armorStandCapture) {
            ArmorStand armorStand = armorStandCapture.pas$getArmorStand();

            if (armorStand == null) {
                return; // Prevent rendering if armor stand is null
            }

            String customName = armorStand.getCustomName() != null ? armorStand.getCustomName().getString() : "";
            PasApi.postEvent(new PasRenderTypePrepareEvent(
                    armorStand, customName, isVisible, appearsGlowing,
                    cir::getReturnValue, cir::setReturnValue
            ));
        }
    }
}
