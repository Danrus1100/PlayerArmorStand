package com.danrus.pas.render;

import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

//$ client
@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public interface ModelWithCape {
    ModelPart getCape();
}
