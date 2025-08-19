package com.danrus.pas.api.event.common;

import com.danrus.pas.api.adapter.ArmorStandAdapter;
import com.danrus.pas.api.types.McSide;
import com.danrus.pas.core.event.PasEvent;

public class PasTickEvent extends PasEvent {

    private final ArmorStandAdapter armorStand;
    private final String customName;
    private final McSide side;

    public PasTickEvent(ArmorStandAdapter armorStand, String hasCustomName, McSide side){
        this.armorStand = armorStand;
        this.customName = hasCustomName;
        this.side = side;
    }

    public ArmorStandAdapter armorStand() {
        return armorStand;
    }
    public String hasCustomName() {
        return customName;
    }

    public McSide side() {
        return side;
    }
}
