package com.danrus.pas.mixin;

import com.danrus.pas.render.item.NameInfoTestItemModel;
import com.danrus.pas.utils.Rl;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModels.class)
public class ItemModelsMixin {

    //? if neoforge {
    /*@Shadow @Final
    private static ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends ItemModel.Unbaked>> ID_MAPPER;
    *///?}


    @Inject(
            method = "bootstrap",
            at = @At("RETURN")
    )
    private static void bootstrapInject(CallbackInfo ci) {
        //? if !neoforge
        ItemModels.
                ID_MAPPER.put(Rl.pas("name_info"), NameInfoTestItemModel.Unbaked.MAP_CODEC);
    }
}
