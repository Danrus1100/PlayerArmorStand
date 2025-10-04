package com.danrus.pas.render.gui.tabs;

import com.danrus.pas.render.gui.widgets.TabButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabManager {
    private final Screen screen;
    private final Map<TabButton, Tab> tabs = new LinkedHashMap<>();

    private Tab activeTab;

    public TabManager(Screen screen) {
        this.screen = screen;
    }

    public void addTab(TabButton button, Tab tab) {
        tabs.put(button, tab);
        button.tabOnPress = b -> setActiveTab(tab);
        // If this is the first tab, make it active.
        if (activeTab == null) {
            setActiveTab(tab);
        } else {
            tab.setVisible(false);
        }
    }

    public void setActiveTab(Tab tab) {
        if (activeTab == tab) {
            return;
        }
        if (activeTab != null) {
            activeTab.setVisible(false);
        }
        activeTab = tab;
        if (activeTab != null) {
            activeTab.setVisible(true);
        }
        // Update button states
        tabs.forEach((btn, t) -> btn.active = (t != activeTab));
    }

    public void init() {
        for (Tab tab : tabs.values()) {
            tab.init(screen);
        }
    }

    public void reposition(int width, int height) {
        if (activeTab != null) {
            activeTab.reposition(width, height);
        }
    }

    public List<AbstractWidget> getAllWidgets() {
        List<AbstractWidget> allWidgets = new ArrayList<>();
        tabs.values().forEach(tab -> allWidgets.addAll(tab.getWidgets()));
        return allWidgets;
    }

    public Tab getActiveTab() {
        return activeTab;
    }

}
