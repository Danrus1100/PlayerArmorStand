package com.danrus.mixin;

import com.danrus.PlayerArmorStands;
import com.danrus.render.ASCapeFeatureRenderer;
import com.danrus.render.PlayerArmorStandModel;
import com.danrus.utils.PASModelData;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
//?}

@Mixin(ArmorStandEntityRenderer.class)
public class ArmorStandEntityRendererMixin {

    //TODO на 1.21.2+ не работает babyModel

    //? if >=1.21.2 {

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void init(EntityRendererFactory.Context context, CallbackInfo ci){
        PlayerArmorStandModel model = new PlayerArmorStandModel(context.getPart(EntityModelLayers.ARMOR_STAND));
        PlayerArmorStands.model = model;
        ArmorStandEntityRenderer renderer = (ArmorStandEntityRenderer) (Object) this;
        renderer.addFeature(new ASCapeFeatureRenderer(renderer));
        ((LivingEntityRenderer)renderer).model = model;
    }

    @Inject(
        method = "getTexture(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;)Lnet/minecraft/util/Identifier;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void getTextureMixin(ArmorStandEntityRenderState armorStandEntityRenderState, CallbackInfoReturnable<Identifier> cir) {
        if (armorStandEntityRenderState.customName == null) {
            return;
        }
        cir.setReturnValue(PASModelData.getByName(armorStandEntityRenderState.customName.getString()).texture);
    }
    //?} else {
    /*@Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void init(EntityRendererFactory.Context context, CallbackInfo ci){
        PlayerArmorStandModel model = new PlayerArmorStandModel(context.getPart(EntityModelLayers.ARMOR_STAND));
        ArmorStandEntityRenderer renderer = (ArmorStandEntityRenderer) (Object) this;
        renderer.addFeature(new ASCapeFeatureRenderer(renderer));
        renderer.model = model;
    }

    @Inject(
            method = "getTexture(Lnet/minecraft/entity/decoration/ArmorStandEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getTextureMixin(ArmorStandEntity armorStandEntity, CallbackInfoReturnable<Identifier> cir){
        if (armorStandEntity.getCustomName() == null) {
            return;
        }
        cir.setReturnValue(PASModelData.getByName(armorStandEntity.getCustomName().getString()).texture);
    }
    *///?}
}
