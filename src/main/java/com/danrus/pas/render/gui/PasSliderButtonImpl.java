package com.danrus.pas.render.gui;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class PasSliderButtonImpl extends AbstractSliderButton {
    public PasSliderButtonImpl(int x, int y, int width, int height, Component message, int value) {
        super(x, y, width, height, message , value / 100.0F);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%d%%", (int) (this.value * 100))));
    }

    @Override
    protected void applyValue() {

    }

    public int getValue() {
        return (int) (this.value * 100);
    }
}
