package com.danrus.mixin;

import com.danrus.utils.ASModelData;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntityModel.class)
public class ArmorStandEntityModelMixin {

//    @Unique
//    private LivingEntityRenderState armorStandEntityRenderState;


    @Inject(
            method = "getTexturedModelData",
            at = @At("RETURN"),
            cancellable = true)
    private static void getTexturedModelData(CallbackInfoReturnable<TexturedModelData> cir) {
        ModelData modelData = PlayerEntityModel.getTexturedModelData(Dilation.NONE, false);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("right_body_stick", ModelPartBuilder.create().uv(0, 0).cuboid(0F, 0F, 0F, 0F, 0F, 0F), ModelTransform.NONE);
        modelPartData.addChild("left_body_stick", ModelPartBuilder.create().uv(0, 0).cuboid(0F, 0F, 0F, 0F, 0F, 0F), ModelTransform.NONE);
        modelPartData.addChild("shoulder_stick", ModelPartBuilder.create().uv(0, 48).cuboid(0F, 0F, 0F, 0F, 0F, 0F), ModelTransform.NONE);
        modelPartData.addChild("base_plate", ModelPartBuilder.create().uv(0, 0).cuboid(0F, 0F, 0F, 0F, 0F, 0F), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
//        ModelPartData modelPartData2 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
//        modelPartData2.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);

        cir.setReturnValue(TexturedModelData.of(modelData, 64, 64));
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;)V",
            at = @At("RETURN")
    )
    private void setAngles(ArmorStandEntityRenderState armorStandEntityRenderState, CallbackInfo ci){
        ((ArmorStandEntityModel) (Object) this).hat.visible = true;
    }
}
