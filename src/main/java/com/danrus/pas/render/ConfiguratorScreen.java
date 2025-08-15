package com.danrus.pas.render;

import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConfiguratorScreen extends Screen {

    private float currentRotation = 0f;
    private float targetRotation = 0f;
    private float currentHeadX = 0f;
    private float currentHeadY = 0f;
    private float currentHeadZ = 0f;
    private float targetHeadX = 0f;
    private float targetHeadY = 0f;
    private float targetHeadZ = 0f;
    private boolean isAnimating = false;
    private static final float ANIMATION_SPEED = 0.15f;


    private final ImageButton rotateButton;
    private final Screen parent;

    public ConfiguratorScreen(Screen parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
        this.rotateButton = new ImageButton(5, 5, 20, 20,
                new WidgetSprites(
                        VersioningUtils.getResourceLocation("pas", "textures/gui/rotate_button.png"),
                        VersioningUtils.getResourceLocation("pas", "textures/gui/rotate_button_disabled.png"),
                        VersioningUtils.getResourceLocation("pas", "textures/gui/rotate_button_highlighted.png")
                ),
                button -> startAnimation()
        );
    }

    @Override
    protected void init() {
//        this.rotateButton.setPosition(Math.round(this.width / 4f + 10) , Math.round(this.height / 2f + 95));
        this.addRenderableWidget(rotateButton);
        subInit(this.width, this.height);
    }

    private void subInit(int width, int height) {
        this.rotateButton.setPosition(Math.round(width / 4f + 10), Math.round(height / 2f + 95));
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        subInit(width, height);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        if (isAnimating) {
            // Update rotation
            currentRotation = lerp(currentRotation, targetRotation, ANIMATION_SPEED);
            // Update head rotation
            currentHeadX = lerp(currentHeadX, targetHeadX, ANIMATION_SPEED);
            currentHeadY = lerp(currentHeadY, targetHeadY, ANIMATION_SPEED);
            currentHeadZ = lerp(currentHeadZ, targetHeadZ, ANIMATION_SPEED);

            // Check if animation is complete
            if (Math.abs(currentRotation - targetRotation) < 0.01f) {
                isAnimating = false;
            }
        }

        ArmorStand entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);
        entity.setCustomName(Component.literal("Danrus110_|C"));
        entity.setNoBasePlate(true);
        entity.setHeadPose(new Rotations(currentHeadX, currentHeadY, currentHeadZ));

        Quaternionf rotation = new Quaternionf().rotateX((float) Math.PI*1.1F)
                .rotateY((float) Math.toRadians(currentRotation));


        InventoryScreen.renderEntityInInventory(
                g, this.width / 4f, this.height / 2f + 85, 85,
                new Vector3f(0, 0, 0),
                rotation,
                null,
                entity
        );
    }

    private float lerp(float start, float end, float speed) {
        return start + (end - start) * speed;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private void startAnimation() {
        isAnimating = true;
        if (targetRotation == 180f) {
            targetRotation = 0f;
        } else {
            targetRotation = 180f;
        }

        if (targetHeadX == 0f) {
            targetHeadX = 10f;
        } else {
            targetHeadX = 0f;
        }

        if (targetHeadY == 0f) {
            targetHeadY = -120f;
        } else {
            targetHeadY = 0f;
        }
    }
}
