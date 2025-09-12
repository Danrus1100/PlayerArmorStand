package com.danrus.pas.utils.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class PasCommandManager {
    public static LiteralArgumentBuilder<PasCommandSource> literal(String name){
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<PasCommandSource, T> argument(String name, ArgumentType<T> type){
        return RequiredArgumentBuilder.argument(name, type);
    }
}
