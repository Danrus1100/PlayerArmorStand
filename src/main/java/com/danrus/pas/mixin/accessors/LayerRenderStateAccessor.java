package com.danrus.pas.mixin.accessors;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface LayerRenderStateAccessor {
    @Accessor("renderType")
    RenderType getRenderType();

    @Accessor("renderType")
    void setRenderType(RenderType renderType);
}
