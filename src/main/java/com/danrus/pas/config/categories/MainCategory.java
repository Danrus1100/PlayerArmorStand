package com.danrus.pas.config.categories;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.config.DownloadStatusDisplay;
import com.danrus.pas.config.YaclConfig;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class MainCategory {
    public static ConfigCategory get(){
        return ConfigCategory.createBuilder()
                .name(Component.translatable("pas.config.group.general"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.general"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.enable_mod"))
                                .binding(
                                        true,
                                        () -> YaclConfig.get().enableMod,
                                        newVal -> YaclConfig.get().enableMod = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.armorstands"))

                        //? !forge {
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.hide_params_on_label"))
                                .binding(
                                        true,
                                        () -> YaclConfig.get().hideParamsOnLabel,
                                        newVal -> YaclConfig.get().hideParamsOnLabel = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        //?}

                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("pas.config.default_skin"))
                                .binding(
                                        "",
                                        () -> YaclConfig.get().defaultSkin,
                                        newVal -> YaclConfig.get().defaultSkin = newVal
                                )
                                .controller(StringControllerBuilder::create)
                                .build())

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.skin_loading"))

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("pas.config.download_threads"))
                                .binding(
                                        3,
                                        () -> YaclConfig.get().downloadThreads, // a getter to getData the current value from
                                        newVal -> YaclConfig.get().downloadThreads = newVal
                                )
                                .controller(IntegerFieldControllerBuilder::create)
                                .addListener(((option, event) -> ModExecutor.reload()))
                                .build())

                        .option(Option.<DownloadStatusDisplay>createBuilder()
                                .name(Component.translatable("pas.config.download_status_display"))
                                .binding(
                                        DownloadStatusDisplay.ABOVE_HOTBAR,
                                        () -> YaclConfig.get().downloadStatusDisplay, // a getter to getData the current value from
                                        newVal -> YaclConfig.get().downloadStatusDisplay = newVal
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(DownloadStatusDisplay.class).formatValue((v) -> Component.translatable("pas.config.download_status_display." + v.name().toLowerCase())))
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.try_apply_from_server_player"))
                                .binding(
                                        true,
                                        () -> YaclConfig.get().tryApplyFromServerPlayer,
                                        newVal -> YaclConfig.get().tryApplyFromServerPlayer = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.show_armor_stand_while_downloading"))
                                .binding(
                                        true,
                                        () -> YaclConfig.get().showArmorStandWhileDownloading,
                                        newVal -> YaclConfig.get().showArmorStandWhileDownloading = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.secret_settings"))
                        .collapsed(true)

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.show_easter_eggs"))
                                .binding(
                                        true,
                                        () -> YaclConfig.get().showEasterEggs,
                                        newVal -> YaclConfig.get().showEasterEggs = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .build();
    }
}

