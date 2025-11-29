package com.danrus.pas.render.armorstand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.Identifier;

public interface Cape {
    void draw(PoseStack stack, Identifier textureLocation, RenderVersionContext context, int light);
}
