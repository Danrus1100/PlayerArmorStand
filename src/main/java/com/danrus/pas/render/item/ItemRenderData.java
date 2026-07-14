package com.danrus.pas.render.item;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import org.jetbrains.annotations.Nullable;

public record ItemRenderData(SkinData skinData, @Nullable CapeData capeData, NameInfo info) {}
