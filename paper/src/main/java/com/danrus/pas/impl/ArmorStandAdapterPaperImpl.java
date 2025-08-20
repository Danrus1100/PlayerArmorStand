package com.danrus.pas.impl;

import com.danrus.pas.api.adapter.ArmorStandAdapter;
import org.bukkit.entity.ArmorStand;

import java.util.UUID;

public class ArmorStandAdapterPaperImpl implements ArmorStandAdapter {

    private final ArmorStand armorStand;

    public ArmorStandAdapterPaperImpl(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public UUID UUID() {
        return armorStand.getUniqueId();
    }

    @Override
    public String customName() {
        return armorStand.getName();
    }

    @Override
    public boolean isInvisible() {
        return armorStand.isInvisible();
    }

    @Override
    public boolean isBaby() {
        return armorStand.isSmall();
    }
}
