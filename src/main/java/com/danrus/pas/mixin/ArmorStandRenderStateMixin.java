//? >=1.21.9 {
package com.danrus.pas.mixin;

import com.danrus.pas.extenders.ArmorStandRenderStateExtender;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmorStandRenderState.class)
public class ArmorStandRenderStateMixin implements ArmorStandRenderStateExtender {

    @Unique
    Component component;

    @Override
    public Component pas$getCustomName() {
        return component;
    }

    @Override
    public void pas$setCustomName(Component component) {
        this.component = component;
    }
}
//?}
