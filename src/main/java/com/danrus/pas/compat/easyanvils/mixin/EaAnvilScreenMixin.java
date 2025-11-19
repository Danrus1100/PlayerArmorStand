package com.danrus.pas.compat.easyanvils.mixin;

import com.danrus.pas.impl.namer.AnvilArmorStandNamer;
import com.danrus.pas.render.gui.PasConfiguratorScreen;
import com.danrus.pas.utils.GuiUtils;
import fuzs.easyanvils.client.gui.screens.inventory.ModAnvilScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModAnvilScreen.class)
public class EaAnvilScreenMixin {
    @Unique
    private Button configuratorButton = GuiUtils.getStandardButtonBuilder(
                    () -> new PasConfiguratorScreen(new AnvilArmorStandNamer((AnvilScreen) (Object) this)))
            .build();

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void pas$init(AnvilMenu menu, Inventory playerInventory, Component title, CallbackInfo ci){
        configuratorButton.visible = false;
    }

    @Inject(
            method = "subInit",
            at = @At("TAIL")
    )
    private void pas$subInit(CallbackInfo ci) {
        GuiUtils.configureButtonOnAnvilScreen(this.configuratorButton, (AnvilScreen) (Object) this);
    }

    @Inject(
            method = "slotChanged",
            at = @At("HEAD")
    )
    private void pas$slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack, CallbackInfo ci){
        GuiUtils.controlButtonActivity(
                () -> dataSlotIndex == 0 && stack.getItem() == Items.ARMOR_STAND,
                () -> dataSlotIndex == 0,
                configuratorButton
        );
    }
}
