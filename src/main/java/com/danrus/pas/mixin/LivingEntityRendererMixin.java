package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.duck.DrawSwapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
//? >=1.21.11
//import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntityRenderState, M extends EntityModel<T>> implements DrawSwapper {

    @Unique
    @Nullable
    private Runnable pas$drawer = null;

    @Override
    public void pas$swapDrawer(Runnable drawer) {
        this.pas$drawer = drawer;
    }

    @Inject(
            method = "isEntityUpsideDown",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void pas$isEntityUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if ((!PasConfig.getInstance().isEnableMod() && !PasConfig.getInstance().isShowEasterEggs())
                || !(entity instanceof ArmorStand)
                || entity.getCustomName() == null
        ) {
            return;
        }

        String name = NameInfo.parse(entity.getCustomName()).base();

        if ((name.equalsIgnoreCase("Dinnerbone")
                || name.equalsIgnoreCase("Grumm")
        ) && entity instanceof ArmorStand) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V")
    )
    private void pas$wrapRender(M instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int l, Operation<Void> original) {
        if (pas$drawer == null) {
            original.call(instance, poseStack, vertexConsumer, i, j ,l);
            return;
        }

        this.pas$drawer.run();
        this.pas$drawer = null;
    }
}
