package com.danrus.pas.mixin.renderstate;

import com.danrus.pas.renderstate.ArmorStandCapture;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmorStandRenderState.class)
public class ArmorStandRenderStateMixin implements ArmorStandCapture {

    @Unique
    private ArmorStand armorStand;

    @Override
    public void pas$setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public ArmorStand pas$getArmorStand() {
        return armorStand;
    }
}
