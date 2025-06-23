package com.danrus.mixin;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.ASModelData;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;

@Mixin(ArmorStandEntityRenderer.class)
public class ArmorStandEntityRendererMixin {

    @Inject(
            method = "getTexture(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getTextureMixin(ArmorStandEntityRenderState armorStandEntityRenderState, CallbackInfoReturnable<Identifier> cir) throws ExecutionException, InterruptedException {
//        String name = armorStandEntityRenderState.customName.getString();
        cir.setReturnValue(ASModelData.getByState(armorStandEntityRenderState).texture);
    }
}
