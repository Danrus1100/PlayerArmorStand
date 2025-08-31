package com.danrus.pas.mixin;

import com.danrus.pas.render.gui.tooltip.PasTooltipComponent;
import com.danrus.pas.utils.StringUtils;
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

//    @Inject(
//            method = "getHoverName",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void pas$getHoverName(CallbackInfoReturnable<Component> cir){
//        ItemStack stack = (ItemStack) (Object) this;
//
//        if (stack.getItem() instanceof ArmorStandItem && stack.getCustomName() != null) {
//            String name = StringUtils.matchASName(stack.getCustomName().getString()).get(0);
//            cir.setReturnValue(Component.literal(name));
//            cir.cancel();
//        }
//    }

    @Inject(
            method = "getTooltipImage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pas$getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.getItem() instanceof ArmorStandItem) {
            cir.setReturnValue(Optional.of(new PasTooltipComponent(stack)));
            cir.cancel();
        }
    }


}
