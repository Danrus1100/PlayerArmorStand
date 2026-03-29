package com.danrus.pas.mixin.accessors;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
    //? <26.1
    @Invoker("renderWidget")
    //? >=26.1
    //@Invoker("extractRenderState")
    void pas$renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
