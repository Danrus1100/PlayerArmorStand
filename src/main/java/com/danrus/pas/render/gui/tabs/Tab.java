package com.danrus.pas.render.gui.tabs;

import com.danrus.pas.mixin.accessors.ScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Tab {
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final BiConsumer<Integer, Integer> layoutManager;
    private final String name;

    public Tab(String name, BiConsumer<Integer, Integer> layoutManager) {
        this.layoutManager = layoutManager;
        this.name = name;
    }

    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    public List<AbstractWidget> getWidgets() {
        return widgets;
    }

    public void setVisible(boolean visible) {
        for (AbstractWidget widget : widgets) {
            widget.visible = visible;
            widget.active = visible;
        }
    }

    public void init(Screen screen) {
        for (AbstractWidget widget : widgets) {
            ((ScreenAccessor) screen).pas$addRenderableWidget(widget);
        }
        reposition(screen.width, screen.height);
    }

    public void reposition(int screenWidth, int screenHeight) {
        layoutManager.accept(screenWidth, screenHeight);
    }

    public String getName() {
        return name;
    }

}