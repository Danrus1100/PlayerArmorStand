package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.LegacySkinData;
import net.minecraft.client.model.geom.ModelPart;

public interface ModelWithCape {
    ModelPart getCape();
    LegacySkinData getData();
}
