package com.danrus.config.categories;

import com.danrus.config.ModConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

public class SimpleCategory {
    public static ConfigCategory get(){
        return ConfigCategory.createBuilder()
                .name(Text.literal("Category Name"))
                .group(OptionGroup.createBuilder()
                        .name(Text.literal("Group Name"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Boolean Option"))
                                .binding(
                                        true,
                                        () -> ModConfig.get().myCoolBoolean, // a getter to get the current value from
                                        newVal -> ModConfig.get().myCoolBoolean = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .build();
    }
}
