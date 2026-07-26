package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.impl.features.DisplayNameFeature;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Unique
    private Component getNameWithoutParams(NameInfo info) {
        DisplayNameFeature feature = info.getFeature(DisplayNameFeature.class);
        if (feature != null && feature.isEnabled()) {
            return Component.literal(feature.getName());
        }
        return Component.literal(info.base());
    }

    @WrapOperation(
            method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V",
            at = @At(value = "INVOKE", target =
                    //? <=26.1
                    "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZIDLnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
                    //? >=26.2
                    //"Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZILnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
            )
    )
    private void pas$submitNameTag
            //? <=26.1 {
            (net.minecraft.client.renderer.SubmitNodeCollector instance, PoseStack poseStack, Vec3 vec3, int i1, Component displayName, boolean b, int i2, double v, net.minecraft.client.renderer.state.level.CameraRenderState cameraRenderState, Operation<Void> original) {
            Consumer<Component> renderer = c -> original.call(instance, poseStack, vec3, i1, c, b, i2, v, cameraRenderState);
            //?} else {
            /*(SubmitNodeCollector instance, PoseStack poseStack, Vec3 vec3, int i1, Component displayName, boolean b, int i2, CameraRenderState cameraRenderState, Operation<Void> original){
            Consumer<Component> renderer = c -> original.call(instance, poseStack, vec3, i1, c, b, i2, cameraRenderState);
            *///?}
        if (displayName.getString().contains("|") && PasConfig.getInstance().isHideParamsOnLabel() && PasConfig.getInstance().isEnableMod()) {
            Component newName = getNameWithoutParams(NameInfo.parse(displayName));
            renderer.accept(newName);
        } else {
            renderer.accept(displayName);
        }
    }
}
