package com.danrus.mixin;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.ASModelData;
import com.danrus.utils.SkinsUtils;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin <S extends LivingEntityRenderState>{
    @Redirect(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;")
    )
    private RenderLayer getRenderLayerMixin(LivingEntityRenderer instance, S state, boolean showBody, boolean translucent, boolean showOutline) {
        if (state instanceof ArmorStandEntityRenderState armorStandEntityRenderState) {
            if (armorStandEntityRenderState.customName == null) return instance.getRenderLayer(state, showBody, translucent, showOutline);
            ASModelData modelData = PlayerArmorStands.modelDataCache.get(armorStandEntityRenderState.customName.getString());
            if (modelData == null) return instance.getRenderLayer(state, showBody, translucent, showOutline);
            Identifier identifier = modelData.texture;
            return RenderLayer.getItemEntityTranslucentCull(identifier != null ? identifier : SkinsUtils.DEFAULT_TEXTURE);
        }
        return instance.getRenderLayer(state, showBody, translucent, showOutline);
    }
}
