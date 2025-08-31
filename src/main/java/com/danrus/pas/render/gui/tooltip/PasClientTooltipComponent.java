package com.danrus.pas.render.gui.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PasClientTooltipComponent implements ClientTooltipComponent {

    PasTooltipComponent component;
    ArmorStand entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);

    public PasClientTooltipComponent(PasTooltipComponent component) {
        this.component = component;
    }

    @Override
    public int getHeight(Font font) {
        return 50;
    }

    @Override
    public int getWidth(Font font) {
        return 50;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        entity.setCustomName(component.fullName());
        Quaternionf rotation = new Quaternionf().rotateX((float) Math.PI * 1.1F)
                .rotateY((float) Math.toRadians(30F));

        //? if <= 1.21.5 {
        InventoryScreen.renderEntityInInventory(
                guiGraphics, x+13, y+42, 21,
                //? >= 1.21.1
                new Vector3f(0, 0, 0),
                rotation,
                null,
                entity
        );
        //?} else {
        /*int right = x + width; // Approx. right boundary
        int bottom = y + 50; // Approx. bottom boundary
        float scale = 21.0F; // Adjust scale as needed
        Vector3f translation = new Vector3f(0f, 0.75f, 0); // Use default translation

        InventoryScreen.renderEntityInInventory(
                guiGraphics,
                x + width / 4,
                y,
                right,
                bottom,
                scale,
                translation,
                rotation,
                null,
                entity
        );
        *///?}
    }
}
