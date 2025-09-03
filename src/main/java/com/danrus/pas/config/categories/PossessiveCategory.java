package com.danrus.pas.config.categories;

import com.danrus.pas.config.ModConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class PossessiveCategory {
    public static ConfigCategory get(){
        return ConfigCategory.createBuilder()
                .name(Component.literal("Possessive"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("pas.config.group.general"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("pas.config.possessive_show_default_hand"))
                                .binding(
                                        false,
                                        () -> ModConfig.get().possessiveShowDefaultHand,
                                        newVal -> ModConfig.get().possessiveShowDefaultHand = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .build())

                .build();
    }
}
