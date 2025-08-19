package com.danrus.pas.api.event.client;

import com.danrus.pas.core.event.PasEvent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.function.Consumer;

public class PasRenderLayerRegistrationEvent
        <S extends
                //? if <= 1.21.1 {
                /*LivingEntity
                 *///?} else {
                net.minecraft.client.renderer.entity.state.LivingEntityRenderState
                //?}
                , M extends EntityModel<? super S>>
        extends PasEvent {
    private final Consumer<RenderLayer<S, M>> layerConsumer;

    public PasRenderLayerRegistrationEvent(Consumer<RenderLayer<S, M>> layerConsumer) {
        this.layerConsumer = layerConsumer;
    }

    public void registerLayer(RenderLayer<S, M> layer) {
        layerConsumer.accept(layer);
    }
}
