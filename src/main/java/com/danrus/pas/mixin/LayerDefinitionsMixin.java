package com.danrus.pas.mixin;

import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {
    @Redirect(
            method = "createRoots",
            at = @At(value = "INVOKE", target =
                    //? <1.21.11
                    "Lnet/minecraft/client/model/ArmorStandModel;createBodyLayer()Lnet/minecraft/client/model/geom/builders/LayerDefinition;"
                    //? >=1.21.11
                    /*"Lnet/minecraft/client/model/object/armorstand/ArmorStandModel;createBodyLayer()Lnet/minecraft/client/model/geom/builders/LayerDefinition;"*/
            )
    )
    private static LayerDefinition pas$redirectArmorStandModelCreateBodyLayer() {
        return PlayerArmorStandModel.createBodyLayer(CubeDeformation.NONE);
    }

}
