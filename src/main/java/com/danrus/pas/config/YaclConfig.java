package com.danrus.pas.config;

//? if yacl {
import com.danrus.pas.config.categories.MainCategory;
import com.danrus.pas.config.categories.PossessiveCategory;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.ModUtils;
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


public class YaclConfig extends PasConfig {
    public static final ConfigClassHandler<YaclConfig> HANDLER = ConfigClassHandler.createBuilder(YaclConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("pas.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    public static List<ConfigCategory> CATEGORIES = new ArrayList<>();

    @ConfigField
    @SerialEntry
    public boolean enableMod = true;

    @ConfigField
    @SerialEntry
    public int downloadThreads = 3;

    @ConfigField
    @SerialEntry
    public DownloadStatusDisplay downloadStatusDisplay = DownloadStatusDisplay.ABOVE_HOTBAR;

    @ConfigField
    @SerialEntry
    public boolean hideParamsOnLabel = true;

    @ConfigField
    @SerialEntry
    public String defaultSkin = "";

    @ConfigField
    @SerialEntry
    public boolean showArmorStandWhileDownloading = true;

    @ConfigField
    @SerialEntry
    public boolean showEasterEggs = true;

    @ConfigField
    @SerialEntry
    public boolean tryApplyFromServerPlayer = true;

    @ConfigField
    @SerialEntry
    public boolean possessiveShowDefaultHand = false;

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Player Armor Stand"))
                .categories(CATEGORIES)
                .save(YaclConfig::save)
                .build()
                .generateScreen(parent);
    }

    public static void initialize() {
        load();
        CATEGORIES.add(MainCategory.get());
        if (ModUtils.isModLoaded("possessive")) {
            CATEGORIES.add(PossessiveCategory.get());
        }
    }

    public static YaclConfig get() {
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

    @Override
    public boolean isEnableMod() {
        return enableMod;
    }

    @Override
    public int getDownloadThreads() {
        return downloadThreads;
    }

    @Override
    public DownloadStatusDisplay getDownloadStatusDisplay() {
        return downloadStatusDisplay;
    }

    @Override
    public boolean isHideParamsOnLabel() {
        return hideParamsOnLabel;
    }

    @Override
    public String getDefaultSkin() {
        return defaultSkin;
    }

    @Override
    public boolean isShowArmorStandWhileDownloading() {
        return showArmorStandWhileDownloading;
    }

    @Override
    public boolean isShowEasterEggs() {
        return showEasterEggs;
    }

    @Override
    public boolean isTryApplyFromServerPlayer() {
        return tryApplyFromServerPlayer;
    }

    @Override
    public boolean isPossessiveShowDefaultHand() {
        return possessiveShowDefaultHand;
    }
}
//?}