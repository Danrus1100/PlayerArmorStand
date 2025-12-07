package com.danrus.pas.render.tooltip;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.RenameFeature;
import com.danrus.pas.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

public class PasClientTooltip implements ClientTooltipComponent {
    public static final Component FEATURES_TEXT = Component.translatable("pas.tooltip.features");

    private NameInfo info;
    private int drawnFeatures = 1;
    ArmorStand entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);

    public PasClientTooltip(TooltipComponent tooltipComponent) {
        if (tooltipComponent instanceof PasTooltip) {
            this.info = ((PasTooltip) tooltipComponent).getNameInfo();
        } else {
            this.info = NameInfo.EMPTY;
        }
    }

    @Override
    public int getHeight(Font font) {
        if (info.isEmpty()) {
            return 0;
        }
        int height = 50;
        int featureCount = 0;
        for (RenameFeature feature : info.getFeatures()) {
            if (feature.getDisplayText() != null) {
                featureCount++;
                if (featureCount == 3) {
                    height += 8;
                } else if (featureCount > 3) {
                    height += 16;
                }
            }
        }
        return height;
    }

    @Override
    public int getWidth(Font font) {
        int width = 30 + font.width(FEATURES_TEXT);
        for (RenameFeature feature : info.getFeatures()) {
            if (feature.getDisplayText() != null) {
                int featureWidth = font.width(feature.getDisplayText()) + 30 + feature.getTextOffset();
                if (featureWidth > width) {
                    width = featureWidth;
                }
            }
        }
        return width;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        if (info.isEmpty()) {
            return;
        }

        entity.setCustomName(Component.literal(info.compile()));
        Quaternionf rotation = new Quaternionf().rotateX((float) Math.PI * 1.1F)
                .rotateY((float) Math.toRadians(30F));

        int right = x + width; // Approx. right boundary
        int bottom = y + 50; // Approx. bottom boundary

        ModUtils.renderEntityOnScreen(
                guiGraphics, rotation,
                x+13, y+42, 21,
                new Vector3f(0f, 0.75f, 0),
                x + width / 4,
                y,
                right,
                bottom,
                entity
        );

        guiGraphics.drawString(font, Component.translatable("pas.tooltip.features"), x + 30, y, Color.YELLOW.getRGB());

        for (RenameFeature feature : info.getFeatures()) {
            drawFeature(font, guiGraphics, feature, x, y);
        }
    }

    private void drawFeature(Font font, GuiGraphics guiGraphics, RenameFeature feature, int x, int y) {
        if (feature.getDisplayText() == null) return;
        guiGraphics.drawString(font, feature.getDisplayText(), x + 30 + feature.getTextOffset(), y + font.lineHeight - 3 + (8 * drawnFeatures), Color.WHITE.getRGB());
        drawnFeatures++;
        if (feature.getIcon() == null) {
            drawnFeatures++;
            return;
        };
        guiGraphics.blitSprite(ModUtils.getGuiRender(), feature.getIcon(), x + 30, y - 7 + (8 * drawnFeatures), 16, 16);
        drawnFeatures++;
    }
}
