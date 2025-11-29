package com.danrus.pas.api.info;

import net.minecraft.resources.Identifier;

public interface InfoTranslator {
    boolean isApplicable(NameInfo info);
    Identifier toIdentifier(NameInfo info);
    String toFileName(NameInfo info);
}
