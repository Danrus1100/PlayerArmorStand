package com.danrus.pas.render.gui;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextWidget extends AbstractWidget {

    public static ResourceLocation QUESTION_MARK_ICON = VersioningUtils.getGuiLocation("pas", "question");

    private boolean hasTooltip = false;
    public TextWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), this.getX() + this.getWidth() / 2 - (hasTooltip ? 4 : 0), this.getY() + (this.getHeight() - Minecraft.getInstance().font.lineHeight) / 2, 16777215 | (int) (this.alpha * 255) << 24);
        if (hasTooltip) {
            //? if >= 1.21.1 {
            guiGraphics.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ QUESTION_MARK_ICON, this.getX() + Minecraft.getInstance().font.width(getMessage()) / 3 + this.getWidth() / 2 + 13, this.getY() + (this.getHeight() - Minecraft.getInstance().font.lineHeight) / 2 - 1, 9, 9);
            //?} else {
            /*guiGraphics.blit(QUESTION_MARK_ICON, this.getX() + Minecraft.getInstance().font.width(getMessage()) / 3 + this.getWidth() / 2 + 13, this.getY() + (this.getHeight() - Minecraft.getInstance().font.lineHeight) / 2 - 1, 0, 0, 9, 9, 9, 9);
            *///?}
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    public TextWidget setTooltip(Component message) {
        Tooltip tooltip = Tooltip.create(message);
        this.setTooltip(tooltip);
        this.hasTooltip = true;
        return this;
    }
}
