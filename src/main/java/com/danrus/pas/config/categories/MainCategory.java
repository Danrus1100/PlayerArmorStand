package com.danrus.pas.config.categories;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.config.ModConfig;
import dev.isxander.yacl3.api.*;
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
                                        () -> ModConfig.get().enableMod,
                                        newVal -> ModConfig.get().enableMod = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("pas.config.download_threads"))
                                .binding(
                                        3,
                                        () -> ModConfig.get().downloadThreads, // a getter to getData the current value from
                                        newVal -> ModConfig.get().downloadThreads = newVal
                                )
                                .controller(IntegerFieldControllerBuilder::create)
                                .addListener(((option, event) -> ModExecutor.reload()))
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.hide_params_on_label"))
                                .binding(
                                        true,
                                        () -> ModConfig.get().hideParamsOnLabel,
                                        newVal -> ModConfig.get().hideParamsOnLabel = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.armorstands"))

                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("pas.config.default_skin"))
                                .binding(
                                        "",
                                        () -> ModConfig.get().defaultSkin,
                                        newVal -> ModConfig.get().defaultSkin = newVal
                                )
                                .controller(StringControllerBuilder::create)
                                .build())

                        .build())
                .build();
    }
}

