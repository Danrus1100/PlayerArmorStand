package com.danrus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin <S extends EntityRenderState>{
    @ModifyVariable(
        method = "renderLabelIfPresent", at = @At("STORE"), ordinal = -1
    )
    private Vec3d setVerticalOffset(Vec3d value, S state){
        if (state instanceof ArmorStandEntityRenderState armorStandEntityRenderState){
            if (armorStandEntityRenderState.small){
//                return value - (int)(armorStandEntityRenderState.baseScale*50);
                return value.add(0, armorStandEntityRenderState.baseScale, 0);
            }
        }
        return value;
    }
}
