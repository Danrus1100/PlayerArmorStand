package com.danrus.pas.mixin.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {
    @Accessor("model")
    <T extends EntityModel<?>> void pas$setModel(T model);

    @Invoker("addLayer")
    <S extends
            //? if <= 1.21.1 {
            /*LivingEntity
             *///?} else {
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState
            //?}
            , M extends EntityModel<? super S>> boolean pas$addLayer(RenderLayer<S, M> layer);
}
