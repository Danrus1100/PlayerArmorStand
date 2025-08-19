package com.danrus.pas.impl.adapter;

import com.danrus.pas.api.adapter.LevelAdapter;
import net.minecraft.server.level.ServerLevel;

public class LevelAdapterModImpl implements LevelAdapter {

    ServerLevel level;

    public LevelAdapterModImpl(ServerLevel level) {
        this.level = level;
    }


    @Override
    public long getWorldTime() {
        return level.getGameTime();
    }
}
