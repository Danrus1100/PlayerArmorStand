package com.danrus.mixin;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.PASModelData;
import com.danrus.utils.StringUtils;
import com.danrus.utils.skin.SkinsUtils;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
        //?}


@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin /*? >=1.21.2 {*/ <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> /*} else {*//* <T extends LivingEntity> *//*?}*/{

    //? if >=1.21.2 {
    @Redirect(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;")
    )
    private RenderLayer getRenderLayerMixin(LivingEntityRenderer instance, S state, boolean showBody, boolean translucent, boolean showOutline) {
        if (state instanceof ArmorStandEntityRenderState armorStandEntityRenderState  && !armorStandEntityRenderState.invisible) {
            if (armorStandEntityRenderState.customName == null) return instance.getRenderLayer(state, showBody, translucent, showOutline);
            PASModelData modelData = PlayerArmorStands.modelDataCache.get(StringUtils.matchASName(armorStandEntityRenderState.customName.getString()).getFirst());
            if (modelData == null) return instance.getRenderLayer(state, showBody, translucent, showOutline);
            Identifier identifier = modelData.texture;
            return RenderLayer.getItemEntityTranslucentCull(identifier != null ? identifier : SkinsUtils.DEFAULT_TEXTURE);
        }
        return instance.getRenderLayer(state, showBody, translucent, showOutline);
    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD")
    )
    private void setModel(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntityRenderState instanceof ArmorStandEntityRenderState) {
            ((LivingEntityRenderer<T, S, M>) (Object) this).model = (M) PlayerArmorStands.model;
        }
    }
    //?} else {
    /*@Redirect(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;")
    )
    public RenderLayer getRenderLayerMixin(LivingEntityRenderer instance, LivingEntity livingEntity, boolean b1, boolean b2, boolean b3) {
        if (livingEntity instanceof ArmorStandEntity armorStandEntity && !armorStandEntity.isInvisible()) {
            if (armorStandEntity.getCustomName() == null) return instance.getRenderLayer(livingEntity, b1, b2, b3);
            PASModelData modelData = PlayerArmorStands.modelDataCache.get(StringUtils.matchASName(armorStandEntity.getCustomName().getString()).getFirst());
            if (modelData == null) return instance.getRenderLayer(livingEntity, b1, b2, b3);
            Identifier identifier = modelData.texture;
            return RenderLayer.getItemEntityTranslucentCull(identifier != null ? identifier : SkinsUtils.DEFAULT_TEXTURE);
        }
        return instance.getRenderLayer(livingEntity, b1, b2, b3);
    }
    *///?}
}
