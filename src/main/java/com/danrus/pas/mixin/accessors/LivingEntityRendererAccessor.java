package com.danrus.pas.mixin.accessors;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {
    @Accessor("model")
    void setModel(EntityModel<?> model);

    @Invoker("addLayer")
    <T extends
            //? if <= 1.21.1 {
            /*net.minecraft.world.entity.Entity
            *///?} else {
            net.minecraft.client.renderer.entity.state.EntityRenderState
            //?}
            , M extends EntityModel<T>> boolean invokeAddLayer(RenderLayer<T, M> layer);
}
