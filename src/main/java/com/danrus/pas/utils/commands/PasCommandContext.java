package com.danrus.pas.utils.commands;

public interface PasCommandContext {
    <T> T getArgument(String name, Class<T> clazz);
    PasCommandSource getSource();
}
