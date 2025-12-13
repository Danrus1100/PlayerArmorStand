package com.danrus.pas.mixin;

import com.danrus.pas.render.item.NameInfoTestItemModel;
import com.danrus.pas.utils.Rl;
import net.minecraft.client.renderer.item.ItemModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModels.class)
public class ItemModelsMixin {
    @Inject(
            method = "bootstrap",
            at = @At("RETURN")
    )
    private static void bootstrapInject(CallbackInfo ci) {
        ItemModels.ID_MAPPER.put(Rl.pas("name_info"), NameInfoTestItemModel.Unbaked.MAP_CODEC);
    }
}
