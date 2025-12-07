package com.danrus.pas.mixin;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.render.tooltip.PasTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "getHoverName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pas$getHoverName(CallbackInfoReturnable<net.minecraft.network.chat.Component> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.getItem() instanceof ArmorStandItem && stack.getCustomName() != null) {
            NameInfo info = NameInfo.parse(stack.getCustomName());
            cir.setReturnValue(Component.literal(info.base()));
        }
    }

    @Inject(
            method = "getTooltipImage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pas$getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.getItem() instanceof ArmorStandItem) {
            cir.setReturnValue(Optional.of(new PasTooltip(stack)));
            cir.cancel();
        }
    }
}
