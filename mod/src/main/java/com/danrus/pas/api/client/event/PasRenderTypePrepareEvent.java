package com.danrus.pas.api.client.event;

import com.danrus.pas.core.event.PasEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PasRenderTypePrepareEvent extends PasEvent {
    private final ArmorStand armorStand;
    private final String customName;
    private final boolean isVisible;
    private final boolean appearsGlowing;
    private final Supplier<RenderType> renderTypeGetter;
    private final Consumer<RenderType> renderTypeSetter;

    public PasRenderTypePrepareEvent(ArmorStand armorStand, String customName, boolean isVisible, boolean appearsGlowing, Supplier<RenderType> renderTypeGetter, Consumer<RenderType> renderTypeSetter) {
        this.armorStand = armorStand;
        this.customName = customName;
        this.isVisible = isVisible;
        this.appearsGlowing = appearsGlowing;
        this.renderTypeGetter = renderTypeGetter;
        this.renderTypeSetter = renderTypeSetter;
    }

    public ArmorStand armorStand() {
        return armorStand;
    }

    public String hasCustomName() {
        return customName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean appearsGlowing() {
        return appearsGlowing;
    }

    public RenderType renderType() {
        return renderTypeGetter.get();
    }

    public void setRenderType(RenderType renderType) {
        renderTypeSetter.accept(renderType);
    }
}
