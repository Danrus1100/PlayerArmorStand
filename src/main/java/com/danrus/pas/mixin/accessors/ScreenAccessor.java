package com.danrus.pas.mixin.accessors;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Invoker("addWidget")
    <T extends GuiEventListener & NarratableEntry> T pas$addWidget(T listener);

    @Invoker("removeWidget")
    void pas$removeWidget(GuiEventListener listener);

    @Invoker("addRenderableWidget")
    <T extends GuiEventListener & Renderable & NarratableEntry> T pas$addRenderableWidget(T widget);
}
