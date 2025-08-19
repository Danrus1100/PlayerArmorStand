package com.danrus.pas.mixin;

import com.danrus.pas.api.types.McSide;
import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.common.PasTickEvent;
import com.danrus.pas.renderstate.ArmorStandCapture;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin implements ArmorStandCapture {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void pas$tick(CallbackInfo ci){
        ArmorStand armorStand = (ArmorStand) (Object) this;
        String customName = armorStand.getCustomName() != null ? armorStand.getCustomName().getString() : "";
        McSide side = armorStand.level().isClientSide() ? McSide.CLIENT : McSide.SERVER;
        PasApi.postEvent(new PasTickEvent(armorStand, customName, side));
    }

    @Inject(
            method = "brokenByAnything",
            at = @At("HEAD")
    )
    private void pas$brokenByAnything(CallbackInfo ci) {
        // This method is intentionally left empty to prevent the default behavior
        // of the ArmorStand being broken by anything, as this mixin is focused on capturing state.
    }

    @Override
    public void pas$setArmorStand(ArmorStand armorStand) {
        // No-op, as this mixin is for the ArmorStand class itself
    }

    @Override
    public ArmorStand pas$getArmorStand() {
        return (ArmorStand) (Object) this;
    }
}
