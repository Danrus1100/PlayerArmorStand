package com.danrus.pas.config;

import com.danrus.pas.config.categories.MainCategory;
import com.danrus.pas.config.categories.PossessiveCategory;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.gui.screens.Screen;

import com.google.gson.GsonBuilder;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;


public class ModConfig {
    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("pas.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    public static List<ConfigCategory> CATEGORIES = new ArrayList<>();

    @SerialEntry
    public boolean enableMod = true;

    @SerialEntry
    public int downloadThreads = 3;

    @SerialEntry
    public DOWNLOAD_STATUS_DISPLAY downloadStatusDisplay = DOWNLOAD_STATUS_DISPLAY.ABOVE_HOTBAR;

    @SerialEntry
    public boolean hideParamsOnLabel = true;

    @SerialEntry
    public String defaultSkin = "";

    @SerialEntry
    public boolean showArmorStandWhileDownloading = true;

    @SerialEntry
    public boolean showEasterEggs = true;

    @SerialEntry
    public boolean tryApplyFromServerPlayer = true;

    @SerialEntry
    public boolean possessiveShowDefaultHand = false;

    public enum DOWNLOAD_STATUS_DISPLAY {
        NONE,
        ABOVE_HOTBAR,
        CHAT
    }

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Player Armor Stand"))
                .categories(CATEGORIES)
                .save(ModConfig::save)
                .build()
                .generateScreen(parent);
    }

    public static void initialize() {
        load();
        CATEGORIES.add(MainCategory.get());
        if (VersioningUtils.isModLoaded("possessive")) {
            CATEGORIES.add(PossessiveCategory.get());
        }
    }

    public static ModConfig get() {
        return HANDLER.instance();
    }

    public static void save() {
        HANDLER.save();
        SkinData.DEFAULT_TEXTURE = HANDLER.instance().showArmorStandWhileDownloading
            ? Rl.vanilla("textures/entity/armorstand/wood.png")
            : Rl.vanilla("textures/entity/player/wide/steve.png");
    }

    public static void load() {
        HANDLER.load();
    }
}