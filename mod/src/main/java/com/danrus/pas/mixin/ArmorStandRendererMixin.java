package com.danrus.pas.mixin;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.client.PasRenderLayerRegistrationEvent;
import com.danrus.pas.core.renderstate.ArmorStandCapture;
import com.danrus.pas.mixin.accessor.LivingEntityRendererAccessor;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public class ArmorStandRendererMixin {
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void pas$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        PasApi.postEvent(new PasRenderLayerRegistrationEvent<>((layer) -> ((LivingEntityRendererAccessor) this).pas$addLayer(layer)));
    }

    //? if >= 1.21.4 {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ArmorStand;Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;F)V",
            at = @At("HEAD")
    )
    private void pas$extractRenderState(ArmorStand armorStand, net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandRenderState, float f, CallbackInfo ci) {
        ((ArmorStandCapture) armorStandRenderState).pas$setArmorStand(armorStand);
    }
    //?}
}
