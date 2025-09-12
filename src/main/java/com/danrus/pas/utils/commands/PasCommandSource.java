package com.danrus.pas.utils.commands;

import net.minecraft.network.chat.Component;

public interface PasCommandSource {
    void fail(Component message);
    void feedback(Component message);
}
