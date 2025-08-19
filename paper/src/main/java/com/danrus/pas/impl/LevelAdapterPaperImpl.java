package com.danrus.pas.impl;

import com.danrus.pas.api.adapter.LevelAdapter;
import org.bukkit.World;

public class LevelAdapterPaperImpl implements LevelAdapter {

    World world;

    public LevelAdapterPaperImpl(World world) {
        this.world = world;
    }

    @Override
    public long getWorldTime() {
        return world.getFullTime();
    }
}
