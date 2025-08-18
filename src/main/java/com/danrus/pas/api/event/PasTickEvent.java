package com.danrus.pas.api.event;

import com.danrus.pas.api.types.McSide;
import com.danrus.pas.core.event.PasEvent;
import net.minecraft.world.entity.decoration.ArmorStand;

public class PasTickEvent extends PasEvent {

    private final ArmorStand armorStand;
    private final String customName;
    private final McSide side;

    public PasTickEvent(ArmorStand armorStand, String hasCustomName, McSide side){
        this.armorStand = armorStand;
        this.customName = hasCustomName;
        this.side = side;
    }

    public ArmorStand armorStand() {
        return armorStand;
    }
    public String hasCustomName() {
        return customName;
    }

    public McSide side() {
        return side;
    }
}
