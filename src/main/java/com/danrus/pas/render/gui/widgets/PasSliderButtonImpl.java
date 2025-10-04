package com.danrus.pas.render.gui.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class PasSliderButtonImpl extends AbstractSliderButton {

    private Consumer<Integer> onChange;
    public PasSliderButtonImpl(int x, int y, int width, int height, Component message, int value, Consumer<Integer> onChange) {
        super(x, y, width, height, message , value / 100.0F);
        this.onChange = onChange;
    }

    @Override
    protected void updateMessage() {
        int intValue = (int) (this.value * 100);

        this.setMessage(Component.literal(String.format("%d%%", intValue)));
        onChange.accept(intValue);
    }

    @Override
    protected void applyValue() {

    }

    public int getValue() {
        return (int) (this.value * 100);
    }
}
