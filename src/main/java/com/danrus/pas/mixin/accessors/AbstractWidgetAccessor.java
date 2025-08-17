package com.danrus.pas.mixin.accessors;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
    @Invoker("renderWidget")
    void pas$renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
