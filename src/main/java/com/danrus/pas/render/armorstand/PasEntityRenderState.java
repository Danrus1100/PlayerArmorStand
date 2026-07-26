package com.danrus.pas.render.armorstand;

import com.danrus.pas.api.info.NameInfo;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.network.chat.Component;

public class PasEntityRenderState extends ArmorStandRenderState {
    public NameInfo info = NameInfo.EMPTY;
    public Component customName = Component.empty();
}
