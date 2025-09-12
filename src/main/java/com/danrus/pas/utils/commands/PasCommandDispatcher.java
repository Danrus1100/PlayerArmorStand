package com.danrus.pas.utils.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public interface PasCommandDispatcher {
    void register(final LiteralArgumentBuilder<PasCommandSource> command);
}
