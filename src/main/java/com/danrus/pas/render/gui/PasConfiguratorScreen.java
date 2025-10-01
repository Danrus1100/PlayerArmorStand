package com.danrus.pas.render.gui;

import com.danrus.pas.mixin.accessors.AnvilScreenAccessor;
import com.danrus.pas.render.gui.tabs.Tab;
import com.danrus.pas.render.gui.tabs.TabButton;
import com.danrus.pas.render.gui.tabs.TabManager;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;

import com.danrus.pas.utils.data.FileTextureCache;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
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

    public static final ResourceLocation ROTATE_BUTTON_TEXTURE = VersioningUtils.getGuiLocation("pas", "rotate_button");
    public static final ResourceLocation ROTATE_BUTTON_DISABLED_TEXTURE = VersioningUtils.getGuiLocation("pas", "rotate_button_disabled");
    public static final ResourceLocation ROTATE_BUTTON_HIGHLIGHTED_TEXTURE = VersioningUtils.getGuiLocation("pas", "rotate_button_highlighted");
    public static final ResourceLocation BACKGROUND_TEXTURE = VersioningUtils.getGuiLocation("pas", "pas_gui");

    public static final ResourceLocation MOJANG_LOGO = VersioningUtils.getGuiLocation("pas", "mojang");
    public static final ResourceLocation NAMEMC_LOGO = VersioningUtils.getGuiLocation("pas", "namemc");
    public static final ResourceLocation FILE_LOGO = VersioningUtils.getGuiLocation("pas", "file");
    public static final ResourceLocation WIDE_ARM_LOGO = VersioningUtils.getGuiLocation("pas", "wide");
    public static final ResourceLocation SLIM_ARM_LOGO = VersioningUtils.getGuiLocation("pas", "slim");

    public static final ResourceLocation YES_LOGO = VersioningUtils.getGuiLocation("pas", "yes");
    public static final ResourceLocation NO_LOGO = VersioningUtils.getGuiLocation("pas", "no");

    private static final float ANIMATION_SPEED = 0.5f;

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

    private final Button acceptButton;
    private final ArmorStand entity;
    private final ArmorStandNamerAdapter parent;

    private final ButtonWithIcon skinProviderButton;
    private final ButtonWithIcon armTypeButton;

    private final TextWidget openFolderLabel;
    private final Button openFolderButton;

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

    public PasConfiguratorScreen(ArmorStandNamerAdapter parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
        this.entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);

        this.acceptButton = Button.builder(Component.translatable("pas.menu.accept"), b -> acceptName()).bounds(width, height/2 + 90, 200, 20).build();

        skinTabButton = new TabButton(5, 5, 80, 15, Component.translatable("pas.menu.tab.skin"));
        capeTabButton = new TabButton(105, 5, 80, 15, Component.translatable("pas.menu.tab.cape"));
        overlayTabButton = new TabButton(205, 5, 80, 15, Component.translatable("pas.menu.tab.overlay"));

        setupParams();

        skinProviderButton = new ButtonWithIcon(0, 0, 120, 20,
                MOJANG_LOGO, Component.translatable("pas.menu.tab.skin.provider." + skinProvider.toLowerCase()),
                button -> changeSkinProvider(skinProvider, button)
                );

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
                hasCape ? YES_LOGO : NO_LOGO,
                hasCape ? Component.translatable("pas.menu.tab.cape.yes") : Component.translatable("pas.menu.tab.cape.no"),
                button -> {
                    hasCape = !hasCape;
                    ((ButtonWithIcon) button).icon = hasCape ? YES_LOGO : NO_LOGO;
                    button.setMessage(Component.translatable("pas.menu.tab.cape." + (hasCape ? "yes" : "no")));
                    setEntityName(entityName);
                });

        openFolderLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.open_folder"));
        openFolderButton = Button.builder(Component.translatable("pas.menu.tab.skin.open_folder.button"), button -> {
            FileTextureCache.SKINS_PATH.toFile().mkdirs();
            Util.getPlatform().openFile(FileTextureCache.SKINS_PATH.toFile());
        }).bounds(0, 0, 120, 20).build();

        this.tabManager = new TabManager(this);

        setupTabs();
    }

    private void setupParams () {

        String itemName = parent.getNameInputValue();

        if (!itemName.equals(Component.translatable("item.minecraft.armor_stand").getString())) {
            List<String> matches = StringUtils.matchASName(parent.getNameInputValue());
            entityName = matches.get(0);

            // FIXME: toooo much if-else statements next:

            if (matches.get(1).contains("S")) {
                isSlim = true;
            }
            if (matches.get(1).contains("C")) {
                hasCape = true;
            }
            if (!matches.get(2).isEmpty()) {
                hasOverlay = true;
                overlayName = matches.get(2);
                overlayBlend = Integer.parseInt(matches.get(3));
            }
            if (matches.get(1).contains("N")) {
                skinProvider = "N";
            } else if (matches.get(1).contains("F")) {
                skinProvider = "F";
            } else {
                skinProvider = "M";
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
        TextWidget nameLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.name")).setTooltip(Component.translatable("pas.menu.tab.skin.name.tooltip"));
        ImageButton acceptNameButton = new ImageButton(0, 0, 20, 20,
                //? >= 1.21.1 {
                new net.minecraft.client.gui.components.WidgetSprites(
                        VersioningUtils.getResourceLocation("pas", "accept"),
                        VersioningUtils.getResourceLocation("pas", "accept_disabled"),
                        VersioningUtils.getResourceLocation("pas", "accept_highlighted")
                ),
                //?} else {
                /*0, 0, 0,
                VersioningUtils.getGuiLocation("pas", "accept"),
                20, 20,
                *///?}
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
            openFolderLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f + 30));
            openFolderButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f + 50));
        });

        skinTab.addWidget(nameBox);
        skinTab.addWidget(nameLabel);
        skinTab.addWidget(acceptNameButton);
        skinTab.addWidget(skinProviderLabel);
        skinTab.addWidget(skinProviderButton);
        skinTab.addWidget(armTypeLabel);
        skinTab.addWidget(armTypeButton);
        skinTab.addWidget(openFolderLabel);
        skinTab.addWidget(openFolderButton);

        // -- Cape Tab ---

        TextWidget capeActiveLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.cape.label"));


        Tab capeTab = new Tab("cape", (width, height) -> {
            capeActiveLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 87));
            capeAciveButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 70));
        });

        capeTab.addWidget(capeActiveLabel);
        capeTab.addWidget(capeAciveButton);

        // --- Overlay Tab ---

        TextWidget blockTextureNameLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.overlay.name")).setTooltip(Component.translatable("pas.menu.tab.overlay.name.tooltip"));
        EnterEditBox blockTextureNameBox = new EnterEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("Overlay Name"), editBox -> {
            overlayName = editBox.getValue();
            hasOverlay = !overlayName.isEmpty();
            overlayBlend = Math.max(0, Math.min(100, overlayBlend)); // Ensure blend is between 0 and 100
            setEntityName(entityName);
        });
        PasSliderButtonImpl overlayBlendSlider = new PasSliderButtonImpl(0, 0, 120, 20, Component.literal(overlayBlend + "%"), overlayBlend);
        ImageButton acceptOverlayNameButton = new ImageButton(0, 0, 20, 20,
                //? >= 1.21.1 {
                new net.minecraft.client.gui.components.WidgetSprites(
                        VersioningUtils.getResourceLocation("pas", "accept"),
                        VersioningUtils.getResourceLocation("pas", "accept_disabled"),
                        VersioningUtils.getResourceLocation("pas", "accept_highlighted")
                ),
                //?} else {
                /*0, 0, 0,
                VersioningUtils.getGuiLocation("pas", "accept"),
                20, 20,
                *///?}
                button -> {
                    overlayName = blockTextureNameBox.getValue();
                    hasOverlay = !overlayName.isEmpty();
                    overlayBlend = Math.max(0, Math.min(100, overlayBlendSlider.getValue())); // Ensure blend is between 0 and 100
                    setEntityName(entityName);
                }
        );
        blockTextureNameBox.setValue(overlayName);
        TextWidget blockTextureBlendLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.overlay.blend"));

        Tab overlayTab = new Tab("overlay", (width, height) -> {
            blockTextureNameLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 87));
            blockTextureNameBox.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 70));
            acceptOverlayNameButton.setPosition(Math.round(width / 2f + 92), Math.round(height / 2f - 70));
            blockTextureBlendLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 50));
            overlayBlendSlider.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 30));
        });

        overlayTab.addWidget(blockTextureNameLabel);
        overlayTab.addWidget(blockTextureNameBox);
        overlayTab.addWidget(acceptOverlayNameButton);
        overlayTab.addWidget(blockTextureBlendLabel);
        overlayTab.addWidget(overlayBlendSlider);


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

//    //? <1.21.9 {
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//    //?} else {
//
//    @Override
//    public boolean keyPressed(KeyEvent keyEvent) {
//        return super.keyPressed(keyEvent);
//    }
//
//    //?

    //? if >= 1.21.1 {
    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(g, mouseX, mouseY, partialTick);
        g.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ BACKGROUND_TEXTURE, this.width / 2 - 128, this.height / 2 - 128 + 18, 256, 256);
    }
    //?} else {
    /*@Override
    public void renderBackground(GuiGraphics g) {
        super.renderBackground(g);
        g.blit(BACKGROUND_TEXTURE, this.width / 2 - 128, this.height / 2 - 128 + 18, 0, 0, 256, 256);
    }
    *///?}

    @Override
    public void tick() {
        super.tick();
        if (tabManager.getActiveTab().getName().equals("cape")) {
            this.currentAnimationState = AnimationState.CAPE;
        } else {
            this.currentAnimationState = AnimationState.IDLE;
        }

        boolean showOpenFolder = "F".equals(skinProvider) && tabManager.getActiveTab().getName().equals("skin");
        openFolderButton.visible = showOpenFolder;
        openFolderLabel.visible = showOpenFolder;
        openFolderButton.active = showOpenFolder;
        openFolderLabel.active = showOpenFolder;

        animateRotation(currentAnimationState);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {

        //? if <= 1.20.1
        /*this.renderBackground(g);*/

        super.render(g, mouseX, mouseY, partialTick);

        if (isAnimating) {
            currentRotation = lerp(currentRotation, targetRotation, ANIMATION_SPEED, partialTick);
            currentHeadX = lerp(currentHeadX, targetHeadX, ANIMATION_SPEED, partialTick);
            currentHeadY = lerp(currentHeadY, targetHeadY, ANIMATION_SPEED, partialTick);
            currentHeadZ = lerp(currentHeadZ, targetHeadZ, ANIMATION_SPEED, partialTick);

            if (Math.abs(currentRotation - targetRotation) < 0.01f) {
                isAnimating = false;
            }
        }

        if (skinProvider.equals("M")) {
            skinProviderButton.icon = MOJANG_LOGO;
        } else if (skinProvider.equals("N")) {
            skinProviderButton.icon = NAMEMC_LOGO;
        } else if (skinProvider.equals("F")) {
            skinProviderButton.icon = FILE_LOGO;
        }

        g.drawCenteredString(Minecraft.getInstance().font, Component.translatable("pas.menu.name"), this.width / 2, 15, 0xFFFFFF);
        entity.setHeadPose(new Rotations(currentHeadX, currentHeadY, currentHeadZ));

        Quaternionf rotation = new Quaternionf().rotateX((float) Math.PI * 1.1F)
                .rotateY((float) Math.toRadians(currentRotation + 30F));

        //? if <= 1.21.5 {
        InventoryScreen.renderEntityInInventory(
                g, (int) (this.width / 2f - 68), (int) (this.height / 2f + 80), 70,
                //? >= 1.21.1
                new Vector3f(0, 0, 0),
                rotation,
                null,
                entity
        );
        //?} else {
        /*int left = this.width / 2 - 130; // Approx. left boundary
        int top = this.height / 2 - 70;  // Approx. top boundary
        int right = this.width / 2 - 18; // Approx. right boundary
        int bottom = this.height / 2 + 120; // Approx. bottom boundary
        float scale = 70.0F; // Adjust scale as needed
        Vector3f translation = new Vector3f(0.1f, 0.75f, 0); // Use default translation

        InventoryScreen.renderEntityInInventory(
                g,
                left,
                top,
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

    private float lerp(float start, float end, float speed, float partialTick) {
        return start + (end - start) * speed * partialTick;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent.getScreen());
        acceptName();
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
                (skinProvider.equals("F") ? "F" : "") +
                (hasOverlay ? "T:" + overlayName + "%" + overlayBlend : "");
    }

    private void acceptName() {
        Minecraft.getInstance().setScreen(parent.getScreen());
        String toAnvil = generateEntityName();
        parent.setNameInputValue(toAnvil);
    }

    private void changeSkinProvider(String literal, Button button) {
        switch (literal) {
            case "M" -> {
                skinProvider = "N";
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.n"));
            }
            case "N" -> {
                skinProvider = "F";
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.f"));
            }
            case "F" -> {
                skinProvider = "M";
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.m"));
            }
        }
    }

    private enum AnimationState {
        IDLE,
        CAPE,
    }
}