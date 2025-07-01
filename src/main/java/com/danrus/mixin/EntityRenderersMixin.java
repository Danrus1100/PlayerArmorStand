package com.danrus.mixin;

import com.danrus.render.PASRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderers.class)
public class EntityRenderersMixin {

    @Shadow
    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {}

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void registerPASRenderer(CallbackInfo ci){
        register(EntityType.ARMOR_STAND, PASRenderer::new);
    }
}
