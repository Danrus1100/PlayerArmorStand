package com.danrus.pas.mixin;

import com.danrus.pas.mixin.accessors.ItemStackRenderStateAccessor;
import com.danrus.pas.mixin.accessors.LayerRenderStateAccessor;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @Shadow public abstract void appendItemLayers(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, @Nullable Level level, @Nullable LivingEntity entity, int seed);

    @Inject(
            method = "updateForTopItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;appendItemLayers(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V")
    )
    private void pas$updateForTopItem(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, Level level, LivingEntity entity, int seed, CallbackInfo ci){
        if (!stack.is(Items.ARMOR_STAND) && stack.getCustomName() == null) {
            return;
        }

        Arrays.stream(((ItemStackRenderStateAccessor) renderState).getLayers()).toList().forEach((state) -> {
            ((LayerRenderStateAccessor) state).setRenderType(RenderType.itemEntityTranslucentCull(SkinManger.getInstance().getSkinTexture(stack.getCustomName())));

        });
    }
}
