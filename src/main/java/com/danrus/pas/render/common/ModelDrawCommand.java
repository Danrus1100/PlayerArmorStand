package com.danrus.pas.render.common;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ModelDrawCommand {
    void draw(ResourceLocation location);
}
