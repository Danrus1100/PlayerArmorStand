package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.ModUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
//? >=1.21.11
/*import net.minecraft.client.renderer.rendertype.RenderType;*/
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
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
        && PasConfig.getInstance().isEnableMod()) || ModUtils.isInvisible(entity)) {
            return;
        }

        NameInfo info;

        if (ModUtils.getCustomName(entity) != null && PasConfig.getInstance().isEnableMod()) {
            info = NameInfo.parse(ModUtils.getCustomName(entity));
        } else if (ModUtils.getCustomName(entity) == null && !PasConfig.getInstance().getDefaultSkin().isEmpty()) {
            info = NameInfo.parse(Component.literal(PasConfig.getInstance().getDefaultSkin()));
        } else {
            info = new NameInfo();
        }

        if (info.lolmeme != null) {
            cir.setReturnValue(RenderType.entitySolid(info.lolmeme));
            return;
        }

        cir.setReturnValue(RenderType.entityTranslucent(PasManager.getInstance().getSkinWithOverlayTexture(info)));
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
}
