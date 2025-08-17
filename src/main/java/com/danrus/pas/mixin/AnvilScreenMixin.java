package com.danrus.pas.mixin;

import com.danrus.pas.mixin.accessors.ScreenAccessor;
import com.danrus.pas.render.gui.PasConfiguratorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
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

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Unique
    private final int buttonWidth = 150;


    @Unique
    private Button configuratorButton = Button.builder(Component.translatable("pas.buttons.configurator"),
            button -> Minecraft.getInstance().setScreen(new PasConfiguratorScreen((AnvilScreen) (Object) this)))
            .tooltip(Tooltip.create(Component.translatable("pas.buttons.configurator.tooltip")))
            .bounds(10, 10, buttonWidth, 20)
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
        AnvilScreen anvilScreen = (AnvilScreen) (Object) this;
        ((ScreenAccessor) this).pas$addRenderableWidget(configuratorButton);
        int i = (anvilScreen.width - buttonWidth) / 2;
        int j = anvilScreen.height / 2 + 87;
        configuratorButton.setPosition(i, j);
    }

    @Inject(
            method = "slotChanged",
            at = @At("HEAD")
    )
    private void pas$slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack, CallbackInfo ci){
        if (dataSlotIndex == 0 && stack.getItem() == Items.ARMOR_STAND) {
            configuratorButton.visible = true;
            configuratorButton.active = true;
        } else if (dataSlotIndex == 0) {
            configuratorButton.visible = false;
            configuratorButton.active = false;
        }
    }
}
