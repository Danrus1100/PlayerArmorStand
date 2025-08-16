package com.danrus.pas.render.gui;

import com.danrus.pas.render.gui.tabs.Tab;
import com.danrus.pas.render.gui.tabs.TabButton;
import com.danrus.pas.render.gui.tabs.TabManager;
import com.danrus.pas.utils.VersioningUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConfiguratorScreen extends Screen {

    public static final ResourceLocation ROTATE_BUTTON_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button");
    public static final ResourceLocation ROTATE_BUTTON_DISABLED_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button_disabled");
    public static final ResourceLocation ROTATE_BUTTON_HIGHLIGHTED_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button_highlighted");
    public static final ResourceLocation BACKGROUND_TEXTURE = VersioningUtils.getResourceLocation("pas", "pas_gui");
    private static final float ANIMATION_SPEED = 0.15f;

    private float currentRotation = 0f;
    private float targetRotation = 0f;
    private float currentHeadX = 0f;
    private float currentHeadY = 0f;
    private float currentHeadZ = 0f;
    private float targetHeadX = 0f;
    private float targetHeadY = 0f;
    private float targetHeadZ = 0f;
    private boolean isAnimating = false;

    private AnimationState currentAnimationState = AnimationState.IDLE;

    private final TabButton skinTabButton;
    private final TabButton capeTabButton;
    private final TabButton overlayTabButton;

    private final ImageButton rotateButton;
    private final ArmorStand entity;
    private final Screen parent;

    private final TabManager tabManager;

    public ConfiguratorScreen(Screen parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
        this.entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);
//        this.entity.setNoBasePlate(true);

        this.rotateButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        ROTATE_BUTTON_TEXTURE,
                        ROTATE_BUTTON_DISABLED_TEXTURE,
                        ROTATE_BUTTON_HIGHLIGHTED_TEXTURE
                ),
                button -> currentAnimationState = currentAnimationState == AnimationState.IDLE ? AnimationState.CAPE : AnimationState.IDLE
        );

        skinTabButton = new TabButton(5, 5, 80, 15, Component.translatable("pas.menu.tab.skin"));
        capeTabButton = new TabButton(105, 5, 80, 15, Component.literal("pas.menu.tab.cape"));
        overlayTabButton = new TabButton(205, 5, 80, 15, Component.literal("pas.menu.tab.overlay"));

        this.tabManager = new TabManager(this);
        setupTabs();
    }

    private void setupTabs() {
        // --- Skin Tab ---
        EditBox nameBox = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("Name"));
        TextWidget nameLabel = new TextWidget(0, 0, 100, 20, Component.literal("test"));
        ImageButton acceptNameButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        VersioningUtils.getResourceLocation("pas", "accept"),
                        VersioningUtils.getResourceLocation("pas", "accept_disabled"),
                        VersioningUtils.getResourceLocation("pas", "accept_highlighted")
                ),
                button -> setEntityName(nameBox.getValue())
        );

        Tab skinTab = new Tab("skin", (width, height) -> {
            nameLabel.setPosition(Math.round(width / 2f ), Math.round(height / 2f - 87));
            nameBox.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 70));
            acceptNameButton.setPosition(Math.round(width / 2f + 92), Math.round(height / 2f - 70));
        });
        skinTab.addWidget(nameBox);
        skinTab.addWidget(nameLabel);
        skinTab.addWidget(acceptNameButton);

        // -- Cape Tab ---


        Tab capeTab = new Tab("cape", (width, height) -> { /* Layout for cape tab */ });


        // --- Overlay Tab ---


        Tab overlayTab = new Tab("overlay", (width, height) -> { /* Layout for overlay tab */ });


        tabManager.addTab(skinTabButton, skinTab);
        tabManager.addTab(capeTabButton, capeTab);
        tabManager.addTab(overlayTabButton, overlayTab);
    }

    @Override
    protected void init() {
//        this.addRenderableWidget(rotateButton); // Removed =(
        tabManager.init();
        repositionElements(this.width, this.height);
    }

    private void repositionElements(int width, int height) {
        this.addRenderableWidget(skinTabButton);
        this.addRenderableWidget(capeTabButton);
        this.addRenderableWidget(overlayTabButton);

        this.rotateButton.setPosition(Math.round(width / 2f - 38), Math.round(height / 2f + 79));
        this.skinTabButton.setPosition(width/2 - 124, height/2 - 109);
        this.capeTabButton.setPosition(width/2 - 43, height/2 - 109);
        this.overlayTabButton.setPosition(width/2 + 38, height/2 - 109);
        tabManager.reposition(width, height);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);

        repositionElements(width, height);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(g, mouseX, mouseY, partialTick);
        g.blitSprite(RenderType::guiTextured, BACKGROUND_TEXTURE, this.width / 2 - 128, this.height / 2 - 128 + 18, 256, 256);
    }

    @Override
    public void tick() {
        super.tick();
        if (tabManager.getActiveTab().getName().equals("cape")) {
            this.rotateButton.active = false;
            this.currentAnimationState = AnimationState.CAPE;
        } else {
            this.rotateButton.active = true;
            this.currentAnimationState = AnimationState.IDLE;
        }

        animateRotation(currentAnimationState);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        if (isAnimating) {
            currentRotation = lerp(currentRotation, targetRotation, ANIMATION_SPEED);
            currentHeadX = lerp(currentHeadX, targetHeadX, ANIMATION_SPEED);
            currentHeadY = lerp(currentHeadY, targetHeadY, ANIMATION_SPEED);
            currentHeadZ = lerp(currentHeadZ, targetHeadZ, ANIMATION_SPEED);

            if (Math.abs(currentRotation - targetRotation) < 0.01f) {
                isAnimating = false;
            }
        }

        g.drawCenteredString(Minecraft.getInstance().font, "Настройка скина", this.width / 2, 15, 0xFFFFFF);
        entity.setHeadPose(new Rotations(currentHeadX, currentHeadY, currentHeadZ));

        Quaternionf rotation = new Quaternionf().rotateX((float) Math.PI * 1.1F)
                .rotateY((float) Math.toRadians(currentRotation + 30F));

        InventoryScreen.renderEntityInInventory(
                g, this.width / 2f - 68, this.height / 2f + 80, 70,
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

    private void setEntityName(String name) {
        if (name != null && !name.isEmpty()) {
            entity.setCustomName(Component.literal(name));
        } else {
            entity.setCustomName(null);
        }
    }

    private void animateRotation(AnimationState state) {
        isAnimating = true;
        switch (state) {
            case IDLE -> {
                targetRotation = 0f;
                targetHeadX = 0f;
                targetHeadY = 0f;
                targetHeadZ = 0f;
            }
            case CAPE -> {
                targetRotation = 180f;
                targetHeadX = 10f;
                targetHeadY = -120f;
                targetHeadZ = 0f;
            }
        }
    }

    private enum AnimationState {
        IDLE,
        CAPE,
    }
}