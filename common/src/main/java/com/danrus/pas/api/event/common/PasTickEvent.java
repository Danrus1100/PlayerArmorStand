package com.danrus.pas.api.event.common;

import com.danrus.pas.api.adapter.ArmorStandAdapter;
import com.danrus.pas.api.types.McSide;
import com.danrus.pas.core.event.PasEvent;

/**
 * Calls only if armor stand has custom name.
 */
public class PasTickEvent extends PasEvent {

    private final ArmorStandAdapter armorStand;
    private final McSide side;

    public PasTickEvent(ArmorStandAdapter armorStand, McSide side){
        this.armorStand = armorStand;
        this.side = side;
    }

    public ArmorStandAdapter armorStand() {
        return armorStand;
    }

    public McSide side() {
        return side;
    }
}
