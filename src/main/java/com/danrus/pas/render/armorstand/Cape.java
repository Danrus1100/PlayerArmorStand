package com.danrus.pas.render.armorstand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public interface Cape {
    void draw(PoseStack stack, ResourceLocation textureLocation, RenderVersionContext context, int light);
}
