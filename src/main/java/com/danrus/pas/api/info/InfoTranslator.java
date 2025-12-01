package com.danrus.pas.api.info;

import net.minecraft.resources.ResourceLocation;

public interface InfoTranslator {
    boolean isApplicable(NameInfo info);
    ResourceLocation toResourceLocation(NameInfo info);
    String toFileName(NameInfo info);
}
