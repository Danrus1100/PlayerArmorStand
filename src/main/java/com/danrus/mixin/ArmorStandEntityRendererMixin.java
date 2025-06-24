package com.danrus.mixin;

import com.danrus.PlayerArmorStands;
import com.danrus.models.SlimPlayerArmorStandModel;
import com.danrus.utils.ASModelData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;

@Mixin(ArmorStandEntityRenderer.class)
public class ArmorStandEntityRendererMixin {

    @Shadow
    @Mutable
    public ArmorStandArmorEntityModel smallModel;

    @Inject(
            method = "getTexture(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getTextureMixin(ArmorStandEntityRenderState armorStandEntityRenderState, CallbackInfoReturnable<Identifier> cir) throws ExecutionException, InterruptedException {
        cir.setReturnValue(ASModelData.getByState(armorStandEntityRenderState).texture);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void init(EntityRendererFactory.Context context, CallbackInfo ci){
        this.smallModel = new SlimPlayerArmorStandModel(context.getPart(EntityModelLayers.PLAYER_SLIM));
    }
}
