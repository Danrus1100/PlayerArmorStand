package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.ModUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
//? >=1.21.11
//import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntityRenderState, M extends EntityModel<T>> {

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
