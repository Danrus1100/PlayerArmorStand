package com.danrus.pas.mixin;

import com.danrus.pas.render.item.ArmorStandSpecialRenderer;
import com.danrus.pas.utils.Rl;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelRenderers.class)
public class SpecialModelRenderersMixin {
    @Inject(
            method = "bootstrap",
            at = @At("RETURN")
    )
    private static void bootstrapInject(CallbackInfo ci) {
        SpecialModelRenderers.ID_MAPPER.put(Rl.pas("armor_stand"), ArmorStandSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
