package com.danrus.pas.mixin;

import com.danrus.pas.config.ModConfig;
import com.danrus.pas.mixin.accessors.LivingEntityRendererAccessor;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
            method = "getRenderType",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void pas$renderType(
            //? if <= 1.21.1 {
            /*LivingEntity
            *///?} else {
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState
            //?}
                    entity, boolean b1, boolean b2, boolean b3, CallbackInfoReturnable<RenderType> cir
    ){
        if (!(entity instanceof
                //? if <= 1.21.1 {
                /*ArmorStand
                *///?} else {
                net.minecraft.client.renderer.entity.state.ArmorStandRenderState
                //?}
        && ModConfig.get().enableMod) || VersioningUtils.isInvisible(entity)) {
            return;
        }

        if (VersioningUtils.getCustomName(entity) != null && ModConfig.get().enableMod) {
            cir.setReturnValue(RenderType.entityTranslucent(SkinManger.getInstance().getSkinTexture(VersioningUtils.getCustomName(entity))));
            cir.cancel();
        } else if (VersioningUtils.getCustomName(entity) == null && !ModConfig.get().defaultSkin.isEmpty()) {
            cir.setReturnValue(RenderType.entityTranslucent(SkinManger.getInstance().getSkinTexture(Component.literal(ModConfig.get().defaultSkin))));
            cir.cancel();
        }
    }

    @Inject(
            method = "isEntityUpsideDown",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void pas$isEntityUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if ((!ModConfig.get().enableMod && !ModConfig.get().showEasterEggs)
                || !(entity instanceof ArmorStand)
                || entity.getCustomName() == null
        ) {
            return;
        }

        if ((StringUtils.matchASName(entity.getCustomName().getString()).get(0).equals("Dinnerbone")
                || StringUtils.matchASName(entity.getCustomName().getString()).get(0).equals("Grumm")
        ) && entity instanceof ArmorStand) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
