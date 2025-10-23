package com.danrus.pas.render.gui;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.render.gui.tabs.Tab;
import com.danrus.pas.render.gui.widgets.TabButton;
import com.danrus.pas.render.gui.tabs.TabManager;
import com.danrus.pas.render.gui.widgets.ButtonWithIcon;
import com.danrus.pas.render.gui.widgets.EnterEditBox;
import com.danrus.pas.render.gui.widgets.PasSliderButtonImpl;
import com.danrus.pas.render.gui.widgets.TextWidget;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.VersioningUtils;

import com.danrus.pas.impl.data.FileTextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class PasConfiguratorScreen extends Screen {

    public static final ResourceLocation BACKGROUND_TEXTURE = Rl.pas("pas_gui");

    public static final ResourceLocation MOJANG_LOGO = Rl.pas("mojang");
    public static final ResourceLocation NAMEMC_LOGO = Rl.pas("namemc");
    public static final ResourceLocation FILE_LOGO = Rl.pas("file");
    public static final ResourceLocation WIDE_ARM_LOGO = Rl.pas("wide");
    public static final ResourceLocation SLIM_ARM_LOGO = Rl.pas("slim");

    public static final ResourceLocation YES_LOGO = Rl.pas("yes");
    public static final ResourceLocation NO_LOGO = Rl.pas("no");

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

    public final TabButton skinTabButton;
    public final TabButton capeTabButton;
    public final TabButton overlayTabButton;

    private final Button acceptButton;
    private final Button cancelButton;
    private final ArmorStand entity;
    private final ArmorStandNamerAdapter parent;

    private final ButtonWithIcon skinProviderButton;
    private final ButtonWithIcon armTypeButton;

    private final TextWidget openFolderLabel;
    private final Button openFolderButton;

    private final ButtonWithIcon capeAciveButton;

    private NameInfo info;

    private String capeProvider = "M";
    private String capeId = "";

    private final TabManager tabManager;

    public PasConfiguratorScreen(ArmorStandNamerAdapter parent) {
        super(Component.literal("Player Armor Stand Configurator"));
        this.parent = parent;
        this.entity = new ArmorStand(Minecraft.getInstance().level, 0, 0, 0);
        this.info = NameInfo.parse(parent.getNameInputValue());
        setEntityName(this.info.compile());

        this.acceptButton = Button.builder(Component.translatable("pas.menu.accept").withStyle(ChatFormatting.GREEN), b -> acceptName()).bounds(width, height/2 - 110, 100, 20).build();
        this.cancelButton = Button.builder(Component.translatable("pas.menu.cancel").withStyle(ChatFormatting.RED), b -> Minecraft.getInstance().setScreen(parent.getScreen())).bounds(width, height/2 + 110, 100, 20).build();

        skinTabButton = new TabButton(5, 5, 80, 15, Component.translatable("pas.menu.tab.skin"));
        capeTabButton = new TabButton(105, 5, 80, 15, Component.translatable("pas.menu.tab.cape"));
        overlayTabButton = new TabButton(205, 5, 80, 15, Component.translatable("pas.menu.tab.overlay"));

        skinProviderButton = new ButtonWithIcon(0, 0, 120, 20,
                MOJANG_LOGO, Component.translatable("pas.menu.tab.skin.provider." + info.getDesiredProvider().toLowerCase()),
                button -> changeSkinProvider(info.getDesiredProvider(), button)
                );

        armTypeButton = new ButtonWithIcon(0, 0, 120, 20,
                info.wantBeSlim() ? SLIM_ARM_LOGO : WIDE_ARM_LOGO,
                Component.translatable("pas.menu.tab.skin.arm_type." + (info.wantBeSlim() ? "slim" : "wide")),
                button -> {
                    info.setSlim(!info.wantBeSlim());
                    ((ButtonWithIcon) button).icon = info.wantBeSlim() ? SLIM_ARM_LOGO : WIDE_ARM_LOGO;
                    button.setMessage(Component.translatable("pas.menu.tab.skin.arm_type." + (info.wantBeSlim() ? "slim" : "wide")));
                    setEntityName(info.compile());
                });

        capeAciveButton = new ButtonWithIcon(0, 0, 120, 20,
                info.wantCape() ? YES_LOGO : NO_LOGO,
                info.wantCape() ? Component.translatable("pas.menu.tab.cape.yes") : Component.translatable("pas.menu.tab.cape.no"),
                button -> {
                    info.setCape(!info.wantCape());
                    ((ButtonWithIcon) button).icon = info.wantCape() ? YES_LOGO : NO_LOGO;
                    button.setMessage(Component.translatable("pas.menu.tab.cape." + (info.wantCape() ? "yes" : "no")));
                    setEntityName(info.compile());
                });

        openFolderLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.open_folder"));
        openFolderButton = Button.builder(Component.translatable("pas.menu.tab.skin.open_folder.button"), button -> {
            FileTextureData.SKINS_PATH.toFile().mkdirs();
            Util.getPlatform().openFile(FileTextureData.SKINS_PATH.toFile());
        }).bounds(0, 0, 120, 20).build();

        this.tabManager = new TabManager(this);

        setupTabs();
    }

    private void setupTabs() {
        // --- Skin Tab ---
        EnterEditBox nameBox = new EnterEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("Name"), editBox -> {
            info.setName(editBox.getValue());
            setEntityName(info.compile());
        });
        nameBox.setValue(info.base());
        TextWidget nameLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.skin.name")).setTooltip(Component.translatable("pas.menu.tab.skin.name.tooltip"));
        ImageButton acceptNameButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        Rl.pas("accept"),
                        Rl.pas("accept_disabled"),
                        Rl.pas("accept_highlighted")
                ),
                button -> {
                    info.setName(nameBox.getValue());
                    setEntityName(info.compile());
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
            armTypeLabel.setPosition(Math.round(width / 2f + 2), Math.round(height / 2f - 50));
            armTypeButton.setPosition(Math.round(width / 2f - 8), Math.round(height / 2f - 30));
        });

        capeTab.addWidget(capeActiveLabel);
        capeTab.addWidget(capeAciveButton);
        capeTab.addWidget(armTypeLabel);
        capeTab.addWidget(armTypeButton);

        // --- Overlay Tab ---

        TextWidget blockTextureNameLabel = new TextWidget(0, 0, 100, 20, Component.translatable("pas.menu.tab.overlay.name")).setTooltip(Component.translatable("pas.menu.tab.overlay.name.tooltip"));
        EnterEditBox blockTextureNameBox = new EnterEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("Overlay Name"), editBox -> {
            info.setOverlay(editBox.getValue());
            info.setBlend(Math.max(0, Math.min(100, info.blend()))); // Ensure blend is between 0 and 100
            setEntityName(info.compile());
        });
        PasSliderButtonImpl overlayBlendSlider = new PasSliderButtonImpl(0, 0, 120, 20, Component.literal(info.blend() + "%"), info.blend(), (i) -> info.setBlend(i));
        ImageButton acceptOverlayNameButton = new ImageButton(0, 0, 20, 20,
                new WidgetSprites(
                        Rl.pas("accept"),
                        Rl.pas("accept_disabled"),
                        Rl.pas("accept_highlighted")
                ),
                button -> {
                    info.setOverlay(blockTextureNameBox.getValue());
                    info.setBlend(Math.max(0, Math.min(100, info.blend()))); // Ensure blend is between 0 and 100
                    setEntityName(info.compile());
                }
        );
        blockTextureNameBox.setValue(info.overlay());
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
        this.addRenderableWidget(cancelButton);
        repositionElements(this.width, this.height);
    }

    private void repositionElements(int width, int height) {
        this.addRenderableWidget(skinTabButton);
        this.addRenderableWidget(capeTabButton);
        this.addRenderableWidget(overlayTabButton);

        this.acceptButton.setPosition(width/2 + 10, height/2 + 120);
        this.cancelButton.setPosition(width/2 - 110, height/2 + 120);

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
        g.blitSprite(/*? >= 1.21.4 {*/VersioningUtils.getGuiRender(),/*?}*/ BACKGROUND_TEXTURE, this.width / 2 - 128, this.height / 2 - 128 + 18, 256, 256);
    }

    @Override
    public void tick() {
        super.tick();
        if (tabManager.getActiveTab().getName().equals("cape")) {
            this.currentAnimationState = AnimationState.CAPE;
        } else {
            this.currentAnimationState = AnimationState.IDLE;
        }

        boolean showOpenFolder = "F".equals(info.getDesiredProvider()) && tabManager.getActiveTab().getName().equals("skin");
        openFolderButton.visible = showOpenFolder;
        openFolderLabel.visible = showOpenFolder;
        openFolderButton.active = showOpenFolder;
        openFolderLabel.active = showOpenFolder;

        animateRotation(currentAnimationState);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {

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

        if (info.getDesiredProvider().equals("M")) {
            skinProviderButton.icon = MOJANG_LOGO;
        } else if (info.getDesiredProvider().equals("N")) {
            skinProviderButton.icon = NAMEMC_LOGO;
        } else if (info.getDesiredProvider().equals("F")) {
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

    private void acceptName() {
        Minecraft.getInstance().setScreen(parent.getScreen());
        String toAnvil = info.compile();
        parent.setNameInputValue(toAnvil);
    }

    private void changeSkinProvider(String literal, Button button) {
        switch (literal) {
            case "M" -> {
                info.setProvider("N");
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.n"));
            }
            case "N" -> {
                info.setProvider("F");
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.f"));
            }
            case "F" -> {
                info.setProvider("M");
                button.setMessage(Component.translatable("pas.menu.tab.skin.provider.m"));
            }
        }
    }

    private enum AnimationState {
        IDLE,
        CAPE,
    }
}