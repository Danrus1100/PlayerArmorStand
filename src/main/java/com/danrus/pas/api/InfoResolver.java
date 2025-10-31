package com.danrus.pas.api;

import net.minecraft.resources.ResourceLocation;

public interface InfoResolver {
    boolean isApplicable(NameInfo info);
    ResourceLocation toResourceLocation(NameInfo info);
    String toFileName(NameInfo info);
}
