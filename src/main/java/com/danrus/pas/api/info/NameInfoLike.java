package com.danrus.pas.api.info;

import org.jetbrains.annotations.NotNull;

public sealed interface NameInfoLike permits NameInfo, NameInfoPattern {
    @NotNull NameInfoPattern toPattern();
}
