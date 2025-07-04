package com.danrus.mixin;

import com.danrus.render.models.PASModel;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Redirect(
            method = "getModels",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ArmorStandEntityModel;getTexturedModelData()Lnet/minecraft/client/model/TexturedModelData;")
    )
    private static TexturedModelData hui(){
        return PASModel.getTexturedModelData(Dilation.NONE);
    }
}
