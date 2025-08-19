package com.danrus.pas.impl.adapter;

import com.danrus.pas.api.adapter.ArmorStandAdapter;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.UUID;

public class ArmorStandAdapterModImpl implements ArmorStandAdapter {

    private final ArmorStand armorStand;

    public ArmorStandAdapterModImpl(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public UUID UUID() {
        return armorStand.getUUID();
    }

    @Override
    public String customName() {
        if (armorStand.getCustomName() == null) {
            return null;
        }
        return armorStand.getCustomName().getString();
    }

    @Override
    public boolean isInvisible() {
        return armorStand.isInvisible();
    }

    @Override
    public boolean isBaby() {
        return armorStand.isBaby();
    }
}
