package com.danrus.pas.mixin;

import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.event.common.PasTickEvent;
import com.danrus.pas.api.types.McSide;
import com.danrus.pas.impl.adapter.ArmorStandAdapterModImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void pas$onTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof ArmorStand armorStand) {
            McSide mcSide = armorStand.level().isClientSide ? McSide.CLIENT : McSide.SERVER;
            PasApi.postEvent(new PasTickEvent(new ArmorStandAdapterModImpl(armorStand), mcSide));
        }
    }
}
