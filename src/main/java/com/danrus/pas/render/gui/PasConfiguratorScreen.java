package com.danrus.pas.render.gui;

import com.danrus.pas.mixin.accessors.AnvilScreenAccessor;
import com.danrus.pas.render.gui.tabs.Tab;
import com.danrus.pas.render.gui.tabs.TabButton;
import com.danrus.pas.render.gui.tabs.TabManager;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class PasConfiguratorScreen extends Screen {

    public static final ResourceLocation ROTATE_BUTTON_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button");
    public static final ResourceLocation ROTATE_BUTTON_DISABLED_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button_disabled");
    public static final ResourceLocation ROTATE_BUTTON_HIGHLIGHTED_TEXTURE = VersioningUtils.getResourceLocation("pas", "rotate_button_highlighted");
    public static final ResourceLocation BACKGROUND_TEXTURE = VersioningUtils.getResourceLocation("pas", "pas_gui");

    public static final ResourceLocation MOJANG_LOGO = VersioningUtils.getResourceLocation("pas", "mojang");
    public static final ResourceLocation NAMEMC_LOGO = VersioningUtils.getResourceLocation("pas", "namemc");

    public static final ResourceLocation WIDE_ARM_LOGO = VersioningUtils.getResourceLocation("pas", "wide");
    public static final ResourceLocation SLIM_ARM_LOGO = VersioningUtils.getResourceLocation("pas", "slim");

    public static final ResourceLocation YES_LOGO = VersioningUtils.getResourceLocation("pas", "yes");
    public static final ResourceLocation NO_LOGO = VersioningUtils.getResourceLocation("pas", "no");

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
    private final Button acceptButton;
    private final ArmorStand entity;
    private final AnvilScreen parent;

    private final ButtonWithIcon skinProviderButton;
    private final ButtonWithIcon armTypeButton;

    private final ButtonWithIcon capeAciveButton;

    private String entityName = "";
    private boolean isSlim = false;
    private String skinProvider = "M";

    private boolean hasCape = false;
    private String capeProvider = "M";
    private String capeId = "";

    private boolean hasOverlay = false;
    private String overlayName = "";
    private int overlayBlend = 100;

    private final TabManager tabManager;

    public PasConfiguratorScreen(AnvilScreen parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
        this.entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);

        this.rotateButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        ROTATE_BUTTON_TEXTURE,
                        ROTATE_BUTTON_DISABLED_TEXTURE,
                        ROTATE_BUTTON_HIGHLIGHTED_TEXTURE
                ),
                button -> currentAnimationState = currentAnimationState == AnimationState.IDLE ? AnimationState.CAPE : AnimationState.IDLE
        );
        this.acceptButton = Button.builder(Component.translatable("pas.menu.accept"), b -> acceptName()).bounds(width, height/2 + 90, 200, 20).build();

        skinTabButton = new TabButton(5, 5, 80, 15, Component.translatable("pas.menu.tab.skin"));
        capeTabButton = new TabButton(105, 5, 80, 15, Component.translatable("pas.menu.tab.cape"));
        overlayTabButton = new TabButton(205, 5, 80, 15, Component.translatable("pas.menu.tab.overlay"));

        setupParams();

        skinProviderButton = new ButtonWithIcon(0, 0, 120, 20,
                MOJANG_LOGO, Component.translatable("pas.menu.tab.skin.provider." + skinProvider.toLowerCase()),
                button -> {
                    if (skinProvider.equals("M")) {
                        skinProvider = "N";
                        button.setMessage(Component.translatable("pas.menu.tab.skin.provider.n"));
                    } else if (skinProvider.equals("N")) {
                        skinProvider = "M";
                        button.setMessage(Component.translatable("pas.menu.tab.skin.provider.m"));
                    }
                });

        armTypeButton = new ButtonWithIcon(0, 0, 120, 20,
                isSlim ? SLIM_ARM_LOGO : WIDE_ARM_LOGO,
                Component.translatable("pas.menu.tab.skin.arm_type." + (isSlim ? "slim" : "wide")),
                button -> {
                    isSlim = !isSlim;
                    ((ButtonWithIcon) button).icon = isSlim ? SLIM_ARM_LOGO : WIDE_ARM_LOGO;
                    button.setMessage(Component.translatable("pas.menu.tab.skin.arm_type." + (isSlim ? "slim" : "wide")));
                    setEntityName(entityName);
                });

        capeAciveButton = new ButtonWithIcon(0, 0, 120, 20,
                hasCape ? YES_LOGO : NO_LOGO, Component.translatable("pas.menu.tab.cape.yes"),
                button -> {
                    hasCape = !hasCape;
                    ((ButtonWithIcon) button).icon = hasCape ? YES_LOGO : NO_LOGO;
                    button.setMessage(Component.translatable("pas.menu.tab.cape." + (hasCape ? "yes" : "no")));
                    setEntityName(entityName);
                });

        this.tabManager = new TabManager(this);

        setupTabs();
    }

    private void setupParams () {

        String itemName = ((AnvilScreenAccessor) parent).pas$getNameInput().getValue();

        if (!itemName.equals(Component.translatable("item.minecraft.armor_stand").getString())) {
            List<String> matches = StringUtils.matchASName(((AnvilScreenAccessor) parent).pas$getNameInput().getValue());
            entityName = matches.get(0);

            // FIXME: toooo much if-else statements next:

            if (matches.get(1).contains("S")) {
                isSlim = true;
            }
            if (matches.get(1).contains("C")) {
                hasCape = true;
            }
            if (matches.get(1).contains("T")) {
                hasOverlay = true;
                overlayName = matches.get(2);
                overlayBlend = Integer.parseInt(matches.get(3));
            }
            if (matches.get(1).contains("N")) {
                skinProvider = "N";
            }

            setEntityName(itemName);

        }
    }

    private void setupTabs() {
        // --- Skin Tab ---
        EnterEditBox nameBox = new EnterEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("Name"), editBox -> {
            entityName = editBox.getValue();
            setEntityName(entityName);
        });
        nameBox.setValue(entityName);
        TextWidget nameLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.name"));
        ImageButton acceptNameButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        VersioningUtils.getResourceLocation("pas", "accept"),
                        VersioningUtils.getResourceLocation("pas", "accept_disabled"),
                        VersioningUtils.getResourceLocation("pas", "accept_highlighted")
                ),
                button -> {
                    entityName = nameBox.getValue();
                    setEntityName(entityName);
                }
        );
        TextWidget skinProviderLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.provider"));
        
        
        TextWidget armTypeLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.arm_type"));
        
        Tab skinTab = new Tab("skin", (width, height) -> {
            nameLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 87));
            nameBox.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 70));
            acceptNameButton.setPosition(Math.round(width / 2f + 92), Math.round(height / 2f - 70));
            skinProviderLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 50));
            skinProviderButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 30));
            armTypeLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 10));
            armTypeButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f + 10));
        });

        skinTab.addWidget(nameBox);
        skinTab.addWidget(nameLabel);
        skinTab.addWidget(acceptNameButton);
        skinTab.addWidget(skinProviderLabel);
        skinTab.addWidget(skinProviderButton);
        skinTab.addWidget(armTypeLabel);
        skinTab.addWidget(armTypeButton);

        // -- Cape Tab ---

        TextWidget capeActiveLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.cape.label"));


        Tab capeTab = new Tab("cape", (width, height) -> {
            capeActiveLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 87));
            capeAciveButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 70));
        });

        capeTab.addWidget(capeActiveLabel);
        capeTab.addWidget(capeAciveButton);

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
        this.addRenderableWidget(acceptButton);
        repositionElements(this.width, this.height);
    }

    private void repositionElements(int width, int height) {
        this.addRenderableWidget(skinTabButton);
        this.addRenderableWidget(capeTabButton);
        this.addRenderableWidget(overlayTabButton);

        this.acceptButton.setPosition(width/2 - 100, height/2 + 120);

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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        System.out.println(keyCode + " " + scanCode + " " + modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
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

        if (skinProvider.equals("M")) {
            skinProviderButton.icon = MOJANG_LOGO;
        } else if (skinProvider.equals("N")) {
            skinProviderButton.icon = NAMEMC_LOGO;
        }

        g.drawCenteredString(Minecraft.getInstance().font, Component.translatable("pas.menu.name"), this.width / 2, 15, 0xFFFFFF);
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
            entity.setCustomName(Component.literal(generateEntityName()));
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

    private String generateEntityName() {
        String idOrName = StringUtils.matchASName(entityName).get(0);

        return idOrName + "|" +
                (isSlim ? "S" : "") +
                (hasCape ? "C" : "") +
                (skinProvider.equals("N") ? "N" : "") +
                (hasOverlay ? "T" + overlayName + "%" + overlayBlend : "");
    }

    private void acceptName() {
        Minecraft.getInstance().setScreen(parent);
        String toAnvil = generateEntityName();
        ((AnvilScreenAccessor) parent).pas$getNameInput().setValue(toAnvil);
    }

    private enum AnimationState {
        IDLE,
        CAPE,
    }
}